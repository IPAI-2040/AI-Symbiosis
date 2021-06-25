package cn.darkfog.ai.symbiosis.assistant.engine.speech.baidu

import android.os.Bundle
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechEvent
import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechToText
import io.reactivex.Completable
import io.reactivex.Observable

object BaiduSTTEngine: SpeechToText {
        override fun init(extra: Bundle?): Completable {
            TODO("Not yet implemented")
        }

        override fun start(extra: Bundle?): Observable<SpeechEvent> {
            TODO("Not yet implemented")
        }

        override fun stop(): Completable {
            TODO("Not yet implemented")
        }

        override fun release(): Completable {
            TODO("Not yet implemented")
        }

    }