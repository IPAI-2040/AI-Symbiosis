package cn.darkfog.dialog_manager.data.source

import cn.darkfog.dialog_manager.model.bean.SpeechRecord
import cn.darkfog.foundation.util.StorageUtil
import io.reactivex.Completable

object RecordRepository {

    fun init(): Completable {
        return PrivateRecordRepository.loadRecords()
    }

    fun addSpeechRecord(record: SpeechRecord): Completable {
        PrivateRecordRepository.records.add(record)
        return PrivateRecordRepository.saveRecords()
    }

    fun getRecordMap(): List<SpeechRecord> =
        PrivateRecordRepository.records.clone() as List<SpeechRecord>

//    fun deleteSpeechRecord(record: SpeechRecord):Completable{
//
//    }

    private object PrivateRecordRepository {
        const val RECORD_FILE = "RECORD_FILE"

        val records = ArrayList<SpeechRecord>()

        fun loadRecords(): Completable {
            return StorageUtil.getCache(ArrayList::class.java, RECORD_FILE)
                .doOnSuccess {
                    records.addAll(it as ArrayList<SpeechRecord>)
                }.ignoreElement()
        }

        fun saveRecords(): Completable {
            return StorageUtil.saveCache(records, RECORD_FILE)
        }
    }

}