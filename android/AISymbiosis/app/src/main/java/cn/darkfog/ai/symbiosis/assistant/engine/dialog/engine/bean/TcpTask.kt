package cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.bean

import android.service.notification.Condition
import cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean.*
import cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.TcpEngine
import cn.darkfog.ai.symbiosis.assistant.resolver.ResolveResult
import cn.darkfog.ai.symbiosis.assistant.resolver.ResolveState
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.reactivex.Observable
import java.lang.Exception
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

//暂时删除dependency,保留dependency字段但是不要使用
//trigger的名字最好改掉
//怎么标记选择
class TcpTask(val taskJson: TaskJson) {
    private val parameters: MutableList<ParameterInstance> = mutableListOf()
    private var currentParameterInstance: ParameterInstance? = null
    private val requiredSlots: MutableSet<String> = mutableSetOf()
    //怎样触发继续？就看能不能消费掉
    //1.当前节点消费
    //2.其他节点消费？根据requiredSlot触发吧

    init {
        taskJson.parameters.forEach { parameter ->
            parameters.add(ParameterInstance(parameter).apply {
                necessary = (parameterInfo.required == "true")
            })
        }
    }

    fun isContinue(input: Input): Boolean = requiredSlots.intersect(input.slotsKey).isNotEmpty()

    fun consumeInput(input: Input):DialogResponse{
        // TODO: 2021/7/14 逻辑处理 
        processCurrentParameter(input)
        processParameters(input)
        return DialogResponse(TaskStatus.Failed,"",JsonObject())
    }

    fun doResolver(resolver:String,input: Input):ResolveResult{
        return ResolveResult()
    }

    fun processParameters(input: Input):ParameterInstance?{
        // TODO: 2021/7/14 填充input 暂时留着吧，先不使用这个字段
        for(parameter in parameters){
            when(parameter.status){
                ParameterStatus.Resolved -> continue
                ParameterStatus.Missed -> {
                    val result = doResolver(parameter.parameterInfo.resolver, input)
                    when (result.status) {
                        ResolveState.UNIQUE -> {
                            parameter.list = result.data
                            if (parameter.parameterInfo.needConfirm) {
                                currentParameterInstance = parameter
                                parameter.status = ParameterStatus.WaitConfirm
                                break
                            }else{
                                continue
                            }
                        }
                        ResolveState.MULTIPLE -> {
                            parameter.list = result.data
                            currentParameterInstance = parameter
                            parameter.status = ParameterStatus.WaitSelect
                            break
                        // TODO: 2021/7/14 进入选择节点
                        }
                        ResolveState.FAIL -> throw Exception()
                    }
                }
                ParameterStatus.WaitSelect,ParameterStatus.WaitConfirm -> throw Exception("")
            }
        }
        return null
    }

        //todo 一段对话思考：选择的时候选择重新选择， mutex还没有处理完成，晚点处理一下
    private fun processCurrentParameter(input: Input):Boolean{
        if (currentParameterInstance == null) return true
        when(currentParameterInstance!!.status) {
            ParameterStatus.Resolved,ParameterStatus.Missed -> {
                throw Exception("condition error")
            }
            ParameterStatus.WaitConfirm -> {
                // TODO: 2021/7/14 处理当前confirm的逻辑
                //currentParameterInstance.status = ParameterStatus.Resolved
            }
            ParameterStatus.WaitSelect -> {
                //处理选择的代码
            }
        }
        return true
    }

}
