package cn.darkfog.ai.symbiosis.context.processor

import cn.darkfog.ai.symbiosis.context.BaseRouter
import cn.darkfog.ai.symbiosis.context.annotation.Action
import cn.darkfog.ai.symbiosis.context.annotation.Request
import cn.darkfog.ai.symbiosis.context.annotation.Domain
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@AutoService(Processor::class)
class RouteProcessor:AbstractProcessor(){
    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elementUtils:Elements

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return hashSetOf(
            Domain::class.java.canonicalName,
            Request::class.java.canonicalName
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils

    }


    override fun process(annotations: MutableSet<out TypeElement>,
                         roundEnvironment: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) return false
        val domainElements = roundEnvironment.getElementsAnnotatedWith(Domain::class.java)
        val requestElements = roundEnvironment.getElementsAnnotatedWith(Request::class.java)
        val actionElements = roundEnvironment.getElementsAnnotatedWith(Action::class.java)


        val requestMap = hashMapOf<String,MutableList<Element>>()
        val actionMap = hashMapOf<String,MutableList<Element>>()
        val domainClzMap = hashMapOf<String,Element>()
        domainElements.forEach { domain ->
            val domainName = domain.getAnnotation(Domain::class.java).name
            if (requestMap.containsKey(domainName)){
                throw Exception("Duplicated Name: $domain")
            }
            requestMap[domainName] = mutableListOf()
            actionMap[domainName] = mutableListOf()
            domainClzMap[domainName] = domain
        }

        requestElements.forEach { action ->
            val domain = action.enclosingElement
            val domainName = domain.getAnnotation(Domain::class.java).name
            if (!requestMap.containsKey(domainName)){
                throw Exception("Not exist Name: $domain")
            }
            requestMap[domainName]!!.add(action)
        }

        actionElements.forEach { action ->
           val domain = action.enclosingElement
            val domainName = domain.getAnnotation(Domain::class.java).name
            if (!requestMap.containsKey(domainName)){
                throw Exception("Not exist Name: $domain")
            }
            actionMap[domainName]!!.add(action)
        }

        //create File
        val pkgName = "cn.darkfog.ai.symbiosis.util"
        val clzName = "AndroidActionRouter"

        //create init block
        val initBlock = CodeBlock.builder()
        for((domainName,elements) in requestMap){
            elements.forEach { element ->
                initBlock.addStatement("addRequestDataFunc(\"$domainName:${element.simpleName}\",%L::%L)\r",
                    "${elementUtils.getPackageOf(domainClzMap[domainName]).qualifiedName}.${domainClzMap[domainName]!!.simpleName}","${element.simpleName}")
            }
        }
        for((domainName,elements) in actionMap){
            elements.forEach { element ->
                initBlock.addStatement("addActionHandler(\"$domainName:${element.simpleName}\",%L::%L)\r",
                    "${elementUtils.getPackageOf(domainClzMap[domainName]).qualifiedName}.${domainClzMap[domainName]!!.simpleName}","${element.simpleName}")
            }
        }

        FileSpec.builder(pkgName,clzName)
            .addType(TypeSpec.objectBuilder(clzName).superclass(BaseRouter::class)
                .addInitializerBlock(initBlock.build())
                .build())
            .build()
            .writeTo(filer)
        return true
    }


}