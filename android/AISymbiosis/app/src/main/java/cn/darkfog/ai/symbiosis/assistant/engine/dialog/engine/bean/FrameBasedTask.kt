package cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.bean

import cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean.*
import cn.darkfog.ai.symbiosis.assistant.resolver.ResolveResult
import cn.darkfog.ai.symbiosis.assistant.resolver.ResolveState
import com.google.gson.JsonObject
import java.lang.Exception

//暂时删除dependency,保留dependency字段但是不要使用
//trigger的名字最好改掉
//怎么标记选择
class FrameBasedTask(val taskJson: TaskJson) {
    private val frames: MutableList<FrameInstance> = mutableListOf()
    private var currentFrameInstance: FrameInstance? = null
    private val requiredSlots: MutableSet<String> = mutableSetOf()
    //怎样触发继续？就看能不能消费掉
    //1.当前节点消费
    //2.其他节点消费？根据requiredSlot触发吧

    init {
        taskJson.parameters.forEach { parameter ->
            frames.add(FrameInstance(parameter).apply {
                necessary = (info.required == "true")
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

    fun processParameters(input: Input):FrameInstance?{
        // TODO: 2021/7/14 填充input 暂时留着吧，先不使用这个字段
        for(frame in frames){
            when(frame.status){
                FrameStatus.Resolved -> continue
                FrameStatus.Missed -> {
                    val result = doResolver(frame.info.resolver, input)
                    when (result.status) {
                        ResolveState.UNIQUE -> {
                            frame.list = result.data
                            if (frame.info.needConfirm) {
                                currentFrameInstance = frame
                                frame.status = FrameStatus.WaitConfirm
                                break
                            }else{
                                continue
                            }
                        }
                        ResolveState.MULTIPLE -> {
                            frame.list = result.data
                            currentFrameInstance = frame
                            frame.status = FrameStatus.WaitSelect
                            break
                        // TODO: 2021/7/14 进入选择节点
                        }
                        ResolveState.FAIL -> throw Exception()
                    }
                }
                FrameStatus.WaitSelect,FrameStatus.WaitConfirm -> throw Exception("error condition")
            }
        }
        return null
    }

        //todo 一段对话思考：选择的时候选择重新选择， mutex还没有处理完成，晚点处理一下
    private fun processCurrentParameter(input: Input):Boolean{
        if (currentFrameInstance == null) return true
        when(currentFrameInstance!!.status) {
            FrameStatus.Resolved,FrameStatus.Missed -> {
                throw Exception("condition error")
            }
            FrameStatus.WaitConfirm -> {
                // TODO: 2021/7/14 处理当前confirm的逻辑
                //currentParameterInstance.status = ParameterStatus.Resolved
            }
            FrameStatus.WaitSelect -> {
                //处理选择的代码
            }
        }
        return true
    }

}
