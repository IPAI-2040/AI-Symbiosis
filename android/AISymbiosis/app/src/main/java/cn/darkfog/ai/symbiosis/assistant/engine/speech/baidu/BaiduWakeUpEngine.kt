package cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu

import android.os.Bundle
import cn.darkfog.ai.symbiosis.assistant.engine.SpeechEventType
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechEvent
import cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUp
import cn.darkfog.ai.symbiosis.assistant.foundation.arch.AppContextContainer
import com.baidu.aip.asrwakeup3.core.wakeup.WakeUpResult
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.SingleEmitter
import org.json.JSONObject
import java.util.*

object BaiduWakeUpEngine:WakeUp {

    private val wp =EventManagerFactory.create(AppContextContainer.context,"wp")

    override fun init(extra: Bundle?): Completable {
        return Completable.create{
            wp.registerListener(Listener)
        }
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
        return Completable.create{
            wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
        }
    }

    override fun release(): Completable {
        return Completable.create{
            wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
        }
    }

    private object Listener : EventListener {
        private const val CALLBACK_EVENT_WAKEUP_STARTED = "wp.enter"
        private const val CALLBACK_EVENT_WAKEUP_READY = "wp.ready"
        private const val CALLBACK_EVENT_WAKEUP_STOPPED = "wp.exit"
        private const val CALLBACK_EVENT_WAKEUP_LOADED = "wp.loaded"
        private const val CALLBACK_EVENT_WAKEUP_UNLOADED = "wp.unloaded"
        private const val CALLBACK_EVENT_WAKEUP_ERROR = "wp.error"
        private const val CALLBACK_EVENT_WAKEUP_SUCCESS = "wp.data"
        private const val CALLBACK_EVENT_WAKEUP_AUDIO = "wp.audio"

        private lateinit var emitter:SingleEmitter<SpeechEvent>

        fun setListener(singleEmitter: SingleEmitter<SpeechEvent>) {
            emitter = singleEmitter
        }

        override fun onEvent(name: String?, params: String?, data: ByteArray?, offset: Int, length: Int) {
            if (!this::emitter.isInitialized) return
            if (emitter.isDisposed) return
            when(name){
                CALLBACK_EVENT_WAKEUP_SUCCESS -> {
                    val result = WakeUpResult.parseJson(name, params)
                    val errorCode = result.errorCode
                    if (result.hasError()) { // error不为0依旧有可能是异常情况
                        emitter.onError(Exception("wake up errorCode:$errorCode"))
                    } else {
                        emitter.onSuccess(SpeechEvent(SpeechEventType.ASR_WUW, result.word))
                    }
                }
                CALLBACK_EVENT_WAKEUP_ERROR -> {
                    val result = WakeUpResult.parseJson(name, params)
                    val errorCode = result.errorCode
                    if (result.hasError()) {
                        emitter.onError(Exception("wake up errorCode:$errorCode"))
                    }
                }
                CALLBACK_EVENT_WAKEUP_STOPPED -> {}
                CALLBACK_EVENT_WAKEUP_AUDIO -> {}

            }
        }
    }
}