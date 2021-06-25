package cn.darkfog.dialog_manager

import android.os.Bundle
import cn.darkfog.dialog_manager.model.bean.SpeechRecord

object MapManager {

    val asrMap = hashMapOf(
        ASR("") to ASR("")
    )

    val nluMap = hashMapOf(
        ASR("") to NLU("", "", Bundle())
    )

    init {
        loadAsrMap()
        loadNluMap()
    }

    fun mapASR(record: SpeechRecord): SpeechRecord {
        record.asr?.let {
            if (asrMap.containsKey(it)) {
                record.asr = asrMap[it]
            }
            if (nluMap.containsKey(it)) {
                record.nlu = nluMap[it]
            }
        }
        return record
    }

    fun addRule(origin: ASR, record: SpeechRecord) {
        record.asr?.let {
            asrMap.put(origin, it)
        }
        record.nlu?.let {
            nluMap.put(origin, it)
        }
    }

    fun deleteRule(origin: ASR) {

    }

    fun loadAsrMap() {

    }

    fun loadNluMap() {

    }

}