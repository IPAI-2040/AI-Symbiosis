package cn.darkfog.speech.protocol.stt

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable

abstract class AbstractSTTEngine {

    val state: MutableLiveData<State> = MutableLiveData(State.NOT_INIT)

    abstract fun init(
        extra: Bundle? = null
    ): Completable

    abstract fun startWakeUp(
        extra: Bundle? = null
    ): Observable<SpeechEvent>

    abstract fun startRecognize(
        extra: Bundle? = null
    ): Observable<SpeechEvent>

    abstract fun stop(
        extra: Bundle? = null
    ): Completable

    abstract fun releaseSpeech(): Completable
}

enum class State {
    NOT_INIT,
    IDLE,
    LISTENING,
    PROCESSING,
    ERROR
}

object STTConstants {
    const val KEY_OF_OUTFILE = "OUTFILE"
}