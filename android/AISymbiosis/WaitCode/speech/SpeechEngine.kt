package cn.darkfog.speech_protocol.speech

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.Serializable

abstract class SpeechEngine(
    val state: MutableLiveData<SpeechState> = MutableLiveData(SpeechState.NOT_INTI)
) {

    abstract fun init(extra: Bundle? = null): Completable

    abstract fun startWakeUp(extra: Bundle? = null): Observable<SpeechEvent>

    abstract fun startRecog(extra: Bundle? = null): Observable<SpeechEvent>

    abstract fun stop(): Completable

    abstract fun destroy(): Completable

}

enum class SpeechState {
    NOT_INTI,
    IDLE,
    LISTENING,
    PROCESSING,
    ERROR
}

data class SpeechEvent(
    val type: SpeechEventType,
    val data: Serializable
)

enum class SpeechEventType {
    VAD_START,
    VAD_END,
    ASR_WUW,
    ASR_PARTIAL,
    ASR_LOCAL,
    ASR_CLOUD,
    NLU_LOCAL,
    NLU_CLOUD
}

data class ASR(
    val content: String,
    val score: Int = 0
) : Serializable

data class Nlu(
    val domain: String,
    val intent: String,
    val slots: Bundle?,
    val score: Int = 0
) : Serializable