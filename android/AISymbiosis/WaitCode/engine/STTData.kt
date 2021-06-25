package cn.darkfog.speech.protocol.stt

import android.os.Bundle
import java.io.Serializable

data class ASR(
    val text: String,
    val score: Int = 0
) : Serializable

data class NLU(
    // TODO: 2020/12/1 add parser
    val parsedText: String,
    val domain: String,
    val intent: String,
    val slots: Bundle,
    val score: Int = 0
) : Serializable

data class Response(
    val text: String,
    val extra: Bundle? = null
) : Serializable