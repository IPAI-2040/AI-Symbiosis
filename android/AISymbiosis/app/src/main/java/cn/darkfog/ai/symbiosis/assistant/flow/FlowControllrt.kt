package cn.darkfog.ai.symbiosis.assistant.flow

import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechEventType
import cn.darkfog.ai.symbiosis.assistant.engine.dialog.engine.FrameBasedEngine
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechToText
import cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUp
import cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu.BaiduSpeechToText
import cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu.BaiduWakeUpEngine
import io.reactivex.Completable

object FlowController {
    private val wakeupEngine:WakeUp = BaiduWakeUpEngine
    private val speechEngine:SpeechToText = BaiduSpeechToText
    private val dialogEngine:FrameBasedEngine  = FrameBasedEngine

    fun init():Completable{
        return wakeupEngine.init()
            .ambWith(speechEngine.init())
            .ambWith(dialogEngine.init())
    }

    fun startLoop(){
        startWakeUp()
    }

    fun startWakeUp(){
        wakeupEngine.start()
            .doAfterSuccess {
                startDialog()
            }
            .subscribe()
    }

    fun startDialog(){
        speechEngine.start()
            .doAfterNext {
                when(it.type){
                    SpeechEventType.VAD_START -> {}
                    SpeechEventType.VAD_END -> {}
                    SpeechEventType.ASR_PARTIAL -> {
                        // TODO: 2021/7/28 显示
                    }
                    SpeechEventType.ASR_LOCAL -> {}
                    SpeechEventType.NLU_LOCAL-> {}
                    SpeechEventType.ASR_CLOUD -> {}
                    SpeechEventType.NLU_CLOUD -> {}
                }
            }.subscribe()


    }

}