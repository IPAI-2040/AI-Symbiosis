package cn.darkfog.speech_service.trash.bean

import android.os.Bundle

data class BaiduNluResult(
    val appid: Int,
    val encoding: String,
    val err_no: Int,
    val parsed_text: String,
    val raw_text: String,
    val results: List<NLUResult>
)

data class NLUResult(
    val domain: String,
    val intent: String,
    val score: Double,
    val slots: Slots
)

data class Slots(
    val user_singer_name: List<Bean>,
    val user_music_name: List<Bean>
) {
    fun toBundle(): Bundle {
        return Bundle().apply {
            if (user_singer_name.isNotEmpty()) {
                putString("singer_name", user_singer_name[0].word)
            }
        }
    }
}

data class Bean(
    val norm: String,
    val word: String
)