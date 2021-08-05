package cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu

import android.os.Bundle
import cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUp
import cn.darkfog.ai.symbiosis.assistant.engine.speech.bean.WakeUpResult
import cn.darkfog.ai.symbiosis.assistant.foundation.arch.AppContextContainer
import cn.darkfog.ai.symbiosis.assistant.foundation.log.logD
import com.baidu.speech.EventListener
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import org.json.JSONObject
import java.util.*

object BaiduWakeUpEngine:WakeUp {

    private val wp =EventManagerFactory.create(AppContextContainer.context,"wp")

    override fun init(extra: Bundle?): Completable {
        return Completable.create{
            wp.registerListener(Listener)
            it.onComplete()
        }
    }

    override fun start(extra: Bundle?): Maybe<cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUpResult> {
        return Maybe.create{
            Listener.setListener(it)
            val params: MutableMap<String, Any> = HashMap()
            params[SpeechConstant.APP_ID] = "24646193"
            params[SpeechConstant.APP_KEY] = "iMHM7stDaowxEbBrWLwNZZ8F"
            params[SpeechConstant.SECRET] = "sekUpue2a2BliKc3H9w5SHVWkyBIK9aa"
            params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
            params[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
            val json: String = JSONObject(params as Map<*, *>).toString()
            logD {  json }
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

        private lateinit var emitter:MaybeEmitter<cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUpResult>

        fun setListener(singleEmitter: MaybeEmitter<cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUpResult>) {
            emitter = singleEmitter
        }

        override fun onEvent(name: String?, params: String?, data: ByteArray?, offset: Int, length: Int) {
            logD {
                "name = $name\n" + "params = $params\n" + "data = ${String(data?: ByteArray(0))}"
            }
            if (!this::emitter.isInitialized) return
            if (emitter.isDisposed) return
            when(name){
                CALLBACK_EVENT_WAKEUP_SUCCESS -> {
                    val result = WakeUpResult.parseJson(name, params)
                    val errorCode = result.errorCode
                    if (result.hasError()) { // error不为0依旧有可能是异常情况
                        emitter.onError(Exception("wake up errorCode:$errorCode"))
                    } else {
                        emitter.onSuccess(cn.darkfog.ai.symbiosis.assistant.engine.speech.WakeUpResult(result.word))
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