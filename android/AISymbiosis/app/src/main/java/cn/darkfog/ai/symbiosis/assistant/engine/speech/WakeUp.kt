package cn.darkfog.ai.symbiosis.assistant.engine.speech;

import android.os.Bundle;

import cn.darkfog.ai.symbiosis.assistant.engine.speech.SpeechEvent;
import io.reactivex.Completable;
import io.reactivex.Maybe
import io.reactivex.Observable

interface WakeUp{
    fun init(extra:Bundle? = null):Completable
    fun start(extra:Bundle? = null): Maybe<SpeechEvent>
    fun stop(): Completable
    fun release(): Completable
}

