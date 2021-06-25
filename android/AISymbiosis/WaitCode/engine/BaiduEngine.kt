package cn.darkfog.ai.symbiosis.assistant.engine

import android.os.Bundle
import cn.darkfog.foundation.arch.AppContextLinker
import cn.darkfog.foundation.log.CLog
import cn.darkfog.foundation.log.logD
import cn.darkfog.foundation.util.GsonHelper
import cn.darkfog.speech.engine.R
import cn.darkfog.speech.protocol.stt.*
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

object BaiduEngine : AbstractSTTEngine(), CLog {

    private lateinit var wakeUpManager: EventManager
    private lateinit var recogManager: EventManager

    //todo get wakeup.bin
    private val wakeUpParams = mapOf(
        SpeechConstant.WP_WORDS_FILE to "assets://WakeUp.bin"
    )

    private val recogParams =
        mapOf(
            SpeechConstant.SOUND_START to R.raw.bdspeech_recognition_start,
            SpeechConstant.SOUND_END to R.raw.bdspeech_recognition_error,
            SpeechConstant.SOUND_CANCEL to R.raw.bdspeech_recognition_cancel,
            SpeechConstant.PID to "15363",
            SpeechConstant.VAD to SpeechConstant.VAD_DNN,
            SpeechConstant.VAD_ENDPOINT_TIMEOUT to 800,
            SpeechConstant.NLU to "enable",
            SpeechConstant.ACCEPT_AUDIO_VOLUME to false,
            SpeechConstant.ACCEPT_AUDIO_DATA to true
        )

    private val offlineParams = JSONObject(
        mapOf(
            SpeechConstant.DECODER to 2,
            SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH to "assets:///baidu_speech_grammar.bsg"
        )
    ).toString()


    override fun init(extra: Bundle?): Completable {
        return Completable.create {
            recogManager = EventManagerFactory.create(AppContextLinker.context, "asr").apply {
                registerListener(BaiduListener)
                send(SpeechConstant.ASR_KWS_LOAD_ENGINE, offlineParams, null, 0, 0)

            }
            wakeUpManager = EventManagerFactory.create(AppContextLinker.context, "wp").apply {
                registerListener(BaiduListener)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    override fun startWakeUp(extra: Bundle?): Observable<SpeechEvent> {
        return Observable.create(ObservableOnSubscribe<SpeechEvent> {
            wakeUpManager.send(
                SpeechConstant.WAKEUP_START,
                JSONObject(wakeUpParams).toString(),
                null,
                0,
                0
            )
            BaiduListener.eventEmitter = it
        }).subscribeOn(AndroidSchedulers.mainThread())
    }

    override fun startRecognize(extra: Bundle?): Observable<SpeechEvent> {
        return Observable.create(ObservableOnSubscribe<SpeechEvent> {
            val outfile = extra?.getString(STTConstants.KEY_OF_OUTFILE)
            val params = recogParams.plus(
                SpeechConstant.OUT_FILE to outfile
            )
            recogManager.send(
                SpeechConstant.ASR_START, JSONObject(params).toString(), null, 0, 0
            )
            BaiduListener.eventEmitter = it
        }).subscribeOn(AndroidSchedulers.mainThread())
    }

    override fun stop(extra: Bundle?): Completable {
        return Completable.create {
            recogManager.send(SpeechConstant.ASR_CANCEL, null, null, 0, 0)
            wakeUpManager.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    override fun releaseSpeech(): Completable {
        return Completable.create {
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }
}


object BaiduListener : EventListener, CLog {
    data class BaiduPartialParams(
        val best_result: String,
        val result_type: String
    )

    data class BaiduResponse(
        val name: String?,
        val params: String?,
        val data: String?,
        val offset: Int,
        val length: Int
    )


    var eventEmitter: ObservableEmitter<SpeechEvent>? = null

    override fun onEvent(
        name: String?,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        if (name == "asr.audio") return

        val response = BaiduResponse(
            name,
            params,
            data?.let { String(it) },
            offset,
            length
        )
        logD {
            response.toString()
        }
        eventEmitter?.run {
            if (isDisposed) return
            when (name) {
                "asr.begin" -> onNext(SpeechEvent(EventType.VAD_START))
                "asr.end" -> onNext(SpeechEvent(EventType.VAD_END))
                "asr.partial" -> {
                    val params =
                        GsonHelper.gson.fromJson(response.params, BaiduPartialParams::class.java)
                    when (params.result_type) {
                        "partial_result" -> onNext(
                            SpeechEvent(
                                EventType.ASR_PARTIAL,
                                ASR(params.best_result)
                            )
                        )
                        "final_result" -> onNext(
                            SpeechEvent(
                                EventType.ASR_CLOUD,
                                ASR(params.best_result)
                            )
                        )
                        "nlu_result" -> {
                            response.data?.let { data ->
                                val nluData = JSONObject(data)
                                val results = nluData.getJSONArray("results")
                                val parsedText = nluData.getString("parsed_text")
                                for (i in 0 until results.length()) {
                                    val result = results.getJSONObject(i)
                                    val slots = result.getJSONObject("slots")
                                    val bundle = Bundle()
                                    val keys = slots.keys()
                                    keys.forEach {
                                        bundle.putString(
                                            it,
                                            slots.getJSONArray(it)
                                                .getJSONObject(0).getString("norm")
                                        )
                                    }

                                    onNext(
                                        SpeechEvent(
                                            EventType.NLU_CLOUD,
                                            NLU(
                                                parsedText = parsedText,
                                                domain = result.getString("domain"),
                                                intent = result.getString("intent"),
                                                score = result.getInt("score"),
                                                slots = bundle
                                            )
                                        )
                                    )
                                }
                            }
                        }
                        else -> Unit
                    }
                }
                "asr.exit" -> onComplete()
                else -> Unit
            }

        }
    }
}


