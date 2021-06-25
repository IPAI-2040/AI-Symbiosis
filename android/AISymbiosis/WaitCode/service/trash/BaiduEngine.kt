package cn.darkfog.speech_service.trash

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import cn.darkfog.foundation.arch.AppContextLinker
import cn.darkfog.foundation.log.CLog
import cn.darkfog.foundation.log.logD
import cn.darkfog.foundation.util.GsonHelper
import cn.darkfog.foundation.util.StorageUtil
import cn.darkfog.speech_protocol.speech.SpeechEngine
import cn.darkfog.speech_service.R
import cn.darkfog.speech_service.trash.bean.BaiduPartialParams
import cn.darkfog.speech_service.trash.bean.BaiduResponse
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import io.reactivex.Completable
import org.json.JSONObject
import java.lang.reflect.Type


/**
 * state和返回值分离
 *
 */
object BaiduEngine : SpeechEngine(),CLog {
    override val logTag: String
        get() = super.logTag
    val state = MutableLiveData(SpeechState.ERROR)
    private var callback: SpeechCallback? = null
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

    private val manager = EventManagerFactory.create(AppContextLinker.context, "asr").apply {
        //设置离线引擎
        send(SpeechConstant.ASR_KWS_LOAD_ENGINE, offlineParams, null, 0, 0)
    }
    private val gson =
        GsonBuilder().registerTypeHierarchyAdapter(NLU::class.java, NLUJsonDeserializer).create()

    fun init(speechCallback: SpeechCallback): Completable {
        return Completable.create {
            callback = speechCallback
            manager.registerListener { name, params, data, offset, length ->

                if (name == "asr.audio") return@registerListener
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
                processBaiduResponse(response).doOnError {
                    callback?.onError(Exception(it))
                }.subscribe()
            }
            state.postValue(SpeechState.IDLE)
            it.onComplete()
        }
    }

    fun start(outfile: String = StorageUtil.AUDIO_PATH + "/" + System.currentTimeMillis() + ".pcm") {
        val params = recogParams.plus(
            SpeechConstant.OUT_FILE to outfile
        )
        manager.send(
            SpeechConstant.ASR_START, JSONObject(params).toString(), null, 0, 0
        )
    }

    fun cancel() {
        manager.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
    }

    fun stop() {
        manager.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0)
    }

    private fun processBaiduResponse(response: BaiduResponse): Completable {
        return Completable.create {
            when (response.name) {
                "asr.partial" -> {
                    val params =
                        GsonHelper.gson.fromJson(response.params, BaiduPartialParams::class.java)

                    when (params.result_type) {
                        "partial_result" -> callback?.onPartialAsrResult(ASR(params.best_result))
                        "final_result" -> callback?.onFinalAsrResult(ASR(params.best_result))
                        "nlu_result" -> {
                            response.data?.let {

                                val nlu = gson.fromJson(
                                    response.data,
                                    NLU::class.java
                                )
                                logD { nlu.toString() }

//
//                                callback?.onFinalNluResult(
//                                    NLU(
//                                        nlu.domain,
//                                        nlu.intent,
//                                        nlu.slots.toBundle()
//                                    )
//                                )
                            }
                        }
                    }
                }
                "asr.begin" -> state.postValue(SpeechState.LISTENING)
                "asr.end" -> state.postValue(SpeechState.PROCESS)
                "asr.finish" -> state.postValue(SpeechState.FINISH)
                "asr.exit" -> state.postValue(SpeechState.IDLE)
                else -> Unit
            }
        }
    }

    object NLUJsonDeserializer : JsonDeserializer<NLU> {
        override fun deserialize(
            element: JsonElement,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): NLU {
            val results = element.asJsonObject.get("results").asJsonArray
            if (results.asJsonArray.size() > 0) {
                val result = element.asJsonObject.get("results").asJsonArray.sortedByDescending {
                    it.asJsonObject.get("score").asDouble
                }[0].asJsonObject
                val domain = result.get("domain").asString
                val intent = result.get("intent").asString
                val slots = Bundle().apply {
                    val data = result.get("slots").asJsonObject
                    data.keySet().forEach {
                        putString(it, data.getAsJsonArray(it)[0].asJsonObject.get("word").asString)
                    }
                }
                return NLU(domain, intent, slots)
            } else {
                return NLU("", "", Bundle())
            }

        }

    }

}