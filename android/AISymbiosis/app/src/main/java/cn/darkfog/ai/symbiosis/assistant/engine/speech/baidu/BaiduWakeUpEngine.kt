package cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu

import android.os.Bundle
import cn.darkfog.ai.symbiosis.assistant.engine.SpeechEventType
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechEvent
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechState
import cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUp
import cn.darkfog.ai.symbiosis.assistant.foundation.arch.AppContextLinker
import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult
import com.baidu.aip.asrwakeup3.core.wakeup.listener.IWakeupListener
import com.baidu.aip.asrwakeup3.core.wakeup.listener.RecogWakeupListener
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.json.JSONObject
import java.lang.Exception
import java.util.*

object BaiduWakeUpEngine:WakeUp {

    val wp =EventManagerFactory.create(AppContextLinker.context,"wp")
    val status = SpeechState.NOT_INTI


    override fun init(extra: Bundle?): Completable {
        return Completable.create{
            wp.registerListener(Listener)

        }
        val listener:IWakeupListener = RecogWakeupListener()
        TODO("Not yet implemented")
    }

    override fun start(extra: Bundle?): Maybe<SpeechEvent> {
        return Maybe.create{
            val params: MutableMap<String, Any> = HashMap()
            params[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
            val json: String = JSONObject(params as Map<*, *>).toString()
            wp.send(SpeechConstant.WAKEUP_START, json, null, 0, 0)
        }
    }

    override fun stop(): Completable {
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
    }

    override fun release(): Completable {
        TODO("Not yet implemented")
    }

    object Listener : EventListener {
        private const val CALLBACK_EVENT_WAKEUP_STARTED = "wp.enter"
        private const val CALLBACK_EVENT_WAKEUP_READY = "wp.ready"
        private const val CALLBACK_EVENT_WAKEUP_STOPPED = "wp.exit"
        private const val CALLBACK_EVENT_WAKEUP_LOADED = "wp.loaded"
        private const val CALLBACK_EVENT_WAKEUP_UNLOADED = "wp.unloaded"
        private const val CALLBACK_EVENT_WAKEUP_ERROR = "wp.error"
        private const val CALLBACK_EVENT_WAKEUP_SUCCESS = "wp.data"
        private const val CALLBACK_EVENT_WAKEUP_AUDIO = "wp.audio"

        private lateinit var emitter:ObservableEmitter<SpeechEvent>

        fun setListener(observableEmitter:ObservableEmitter<SpeechEvent>) {
            emitter = observableEmitter
        }

        override fun onEvent(name: String?, params: String?, data: ByteArray?, offset: Int, length: Int) {
            if (!this::emitter.isInitialized) return
            when(name){
                CALLBACK_EVENT_WAKEUP_SUCCESS -> {
                    val result = WakeUpResult.parseJson(name, params)
                    val errorCode = result.errorCode
                    if (result.hasError()) { // error不为0依旧有可能是异常情况
                        emitter.onError(Exception("wake up errorCode:$errorCode"))
                    } else {
                        val word = result.word
                        emitter.onNext(SpeechEvent(SpeechEventType.ASR_WUW,))
                        listener.onSuccess(word, result)

                }
                CALLBACK_EVENT_WAKEUP_ERROR -> {}
                CALLBACK_EVENT_WAKEUP_STOPPED -> {}
                CALLBACK_EVENT_WAKEUP_AUDIO -> {}

            }
            if(CALLBACK_EVENT_WAKEUP_SUCCESS == name){


            }        }


    }
}