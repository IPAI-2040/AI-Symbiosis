package cn.darkfog.ai.symbiosis.context.processor

import cn.darkfog.ai.symbiosis.context.BaseRouter
import cn.darkfog.ai.symbiosis.context.annotation.Action
import cn.darkfog.ai.symbiosis.context.annotation.Domain
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

@AutoService(Processor::class)
class RouteProcessor:AbstractProcessor(){
    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elementUtils:Elements

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return hashSetOf(
            Domain::class.java.canonicalName,
            Action::class.java.canonicalName
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
        val domainElements = roundEnvironment.getElementsAnnotatedWith(Domain::class.java)
        val actionElements = roundEnvironment.getElementsAnnotatedWith(Action::class.java)

        val domainMap = hashMapOf<String,MutableList<Element>>()
        val domainClzMap = hashMapOf<String,String>()
        domainElements.forEach { domain ->
            val domainName = domain.getAnnotation(Domain::class.java).name
            if (domainMap.containsKey(domainName)){
                throw Exception("Duplicated Name: $domain")
            }
            domainMap[domainName] = mutableListOf()
            domainClzMap[domainName] = domain.simpleName.toString()
        }

        actionElements.forEach { action ->
           val domain = action.enclosingElement
            val domainName = domain.getAnnotation(Domain::class.java).name
            if (!domainMap.containsKey(domainName)){
                throw Exception("Not exist Name: $domain")
            }
            domainMap[domainName]!!.add(action)
        }

        //create File
        val pkgName = "cn.darkfog.ai.symbiosis.util"
        val clzName = "AndroidActionRouter"

        //create init block
        val initBlock = CodeBlock.builder()
        for((domainName,elements) in domainMap){
            elements.forEach { element ->
                initBlock.addStatement("addRequestDataFunc(\"$domainName:${element.simpleName}\",%L::%L)\r\n","${domainClzMap[domainName]}","${element.simpleName}")
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