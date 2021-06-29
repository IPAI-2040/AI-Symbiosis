package cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu

import android.os.Bundle
import cn.darkfog.ai.symbiosis.assistant.R
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechEvent
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechToText
import cn.darkfog.ai.symbiosis.assistant.foundation.arch.AppContextLinker
import cn.darkfog.ai.symbiosis.assistant.foundation.util.StorageUtil
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import io.reactivex.Completable
import io.reactivex.Observable
import org.json.JSONArray
import org.json.JSONObject

object BaiduSpeechToText : SpeechToText {
    // SDK集成步骤 初始化asr的EventManager示例，多次得到的类，只能选一个使用
    val asr = com.baidu.speech.EventManagerFactory.create(AppContextLinker.context, "asr")

    override fun init(extra: Bundle?): Completable {
        return Completable.create {
            val map: MutableMap<String, Any> = HashMap()
            map[SpeechConstant.DECODER] = 2
            map[SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH] =
                "assets:///baidu_speech_grammar.bsg"
            val slotJson = JSONObject()
            slotJson.put("name", JSONArray().put("妈妈").put("李四"))
                    .put("appname", JSONArray().put("手百").put("度秘"))
                map[SpeechConstant.SLOT_DATA] = slotJson
            // 基于DEMO集成1.4 加载离线资源步骤(离线时使用)。offlineParams是固定值，复制到您的代码里即可
            val json: String = JSONObject(map as Map<*, *>).toString()
            asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, json, null, 0, 0)
        }
    }

    override fun start(extra: Bundle?): Observable<SpeechEvent> {
        return Observable.create{
            // SDK集成步骤 拼接识别参数
            val map: HashMap<String, Any> = HashMap()
            // 声音回调
            map.put(SpeechConstant.SOUND_START, R.raw.bdspeech_recognition_start)
            map.put(SpeechConstant.SOUND_END, R.raw.bdspeech_speech_end)
            map.put(SpeechConstant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success)
            map.put(SpeechConstant.SOUND_ERROR, R.raw.bdspeech_recognition_error)
            map.put(SpeechConstant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel)
            // 保存录音文件
            map[SpeechConstant.ACCEPT_AUDIO_DATA] = true // 目前必须开启此回掉才嫩保存音频
            val samplePath = StorageUtil.AUDIO_PATH
            map[SpeechConstant.OUT_FILE] = "$samplePath/outfile.pcm"
            val json: String = JSONObject(map as Map<*, *>).toString()
            asr.send(SpeechConstant.ASR_START, json, null, 0, 0)
        }
    }

    override fun stop(): Completable {
        return Completable.create {
            asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0)
            it.onComplete()
        }
    }

    override fun release(): Completable {
        return Completable.create {
            asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0)
            asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0)
            it.onComplete()
        }
    }

    private object RecogListener: EventListener{
        private const val CALLBACK_EVENT_ASR_LOADED = "asr.loaded"
        private const val  CALLBACK_EVENT_ASR_UNLOADED = "asr.unloaded"

        private const val CALLBACK_EVENT_ASR_SERIALNUMBER = "asr.sn"
        private const val  CALLBACK_EVENT_UPLOAD_COMPLETE = "asr.upload.complete"
        private const val CALLBACK_EVENT_ASR_LONG_SPEECH = "asr.long-speech.finish"

        // 引擎准备就绪，可以开始说话
        private const val CALLBACK_EVENT_ASR_READY = "asr.ready"
        //检测到用户的已经开始说话
        private const val  CALLBACK_EVENT_ASR_BEGIN = "asr.begin"

        private const val CALLBACK_EVENT_ASR_PARTIAL = "asr.partial"
        // 检测到用户的已经停止说话
        private const val CALLBACK_EVENT_ASR_END = "asr.end"

        private const val CALLBACK_EVENT_ASR_AUDIO = "asr.audio"
        private const val CALLBACK_EVENT_ASR_VOLUME = "asr.volume"

        private const val  CALLBACK_EVENT_ASR_FINISH = "asr.finish"
        private const val  CALLBACK_EVENT_ASR_EXIT = "asr.exit"
        private const val  CALLBACK_EVENT_ASR_CANCEL = "asr.cancel"
        private const val  CALLBACK_EVENT_ASR_ERROR = "asr.error"
        override fun onEvent(p0: String?, p1: String?, p2: ByteArray?, p3: Int, p4: Int) {
            TODO("Not yet implemented")
        }


    }
}