package cn.darkfog.ai.symbiosis.assistant.engine.speech

import android.os.Bundle
import cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu.BaiduSpeechToText
import io.reactivex.Completable
import io.reactivex.Observable

interface SpeechToText{
    fun init(extra:Bundle? = null):Completable
    fun start(extra:Bundle? = null):Observable<SpeechEvent>
    fun stop(): Completable
    fun release(): Completable
    companion object{
        fun getSpeechToTextInstance():SpeechToText{
            return BaiduSpeechToText
        }
    }
}