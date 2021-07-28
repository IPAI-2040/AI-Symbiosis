package cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine

import cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean.*
import cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.bean.FrameBasedTask
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import java.lang.UnsupportedOperationException

object FrameBasedEngine {

    private val triggerMap:MutableMap<String,String> = mutableMapOf()
    private val taskCache:MutableMap<String,TaskJson> = mutableMapOf()
    private lateinit var currentTask: FrameBasedTask

    fun init():Completable{

        return Completable.complete()
    }
    //需不需要全部加载进来？需要测试一下延迟，然后制定
    fun init(triggerList: TriggerList){
        triggerList.triggers.forEach { trigger ->
            trigger.intents.forEach { intent ->
                triggerMap[intent] = trigger.path
            }
            if (trigger.loadWhenInit){
                loadTask(trigger.path)
            }
        }
    }

    fun loadTask(path:String){
        // TODO: 2021/7/12
        //taskCache[path] = TaskJson()
    }

    fun execute(input: Input):Single<DialogResponse>{
        return Single.create(SingleOnSubscribe<FrameBasedTask> { emitter ->
            if (FrameBasedEngine::currentTask.isInitialized && currentTask.isContinue(input)) emitter.onSuccess(currentTask)
            if (((FrameBasedEngine::currentTask.isInitialized && currentTask.taskJson.breakable) || !FrameBasedEngine::currentTask.isInitialized)
                && triggerMap.containsKey(input.triggerString)) emitter.onSuccess(FrameBasedTask(taskCache[input.triggerString]!!))
            throw UnsupportedOperationException("${input.triggerString} not matched")
        }).map { task ->
            task.consumeInput(input)
        }
    }


}
