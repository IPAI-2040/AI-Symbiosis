package cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine

import cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean.*
import cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.bean.TcpTask
import com.google.gson.JsonObject
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import java.lang.UnsupportedOperationException

object TcpEngine {

    private val triggerMap:MutableMap<String,String> = mutableMapOf()
    private val taskCache:MutableMap<String,TaskJson> = mutableMapOf()
    private lateinit var currentTask: TcpTask

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
        return Single.create(object : SingleOnSubscribe<TcpTask>{
            override fun subscribe(emitter: SingleEmitter<TcpTask>) {
                if (TcpEngine::currentTask.isInitialized && currentTask.isContinue(input)) emitter.onSuccess(currentTask)
                if (((TcpEngine::currentTask.isInitialized && currentTask.taskJson.breakable) || !TcpEngine::currentTask.isInitialized)
                    && triggerMap.containsKey(input.triggerString)) emitter.onSuccess(TcpTask(taskCache[input.triggerString]!!))
                throw UnsupportedOperationException("${input.triggerString} not matched")
            }
        }).map { task ->
            task.consumeInput(input)
        }
    }


}
