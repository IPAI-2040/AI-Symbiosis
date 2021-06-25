package cn.darkfog.ai.symbiosis.assistant.foundation.util

import cn.darkfog.ai.symbiosis.assistant.foundation.arch.AppContextLinker
import io.reactivex.Completable
import io.reactivex.Single
import java.io.*

object StorageUtil {
    private const val AUDIO_FILE = "audio"
    private const val CACHE_FILE = "cache"

    private const val ASR_RULE_FILE = "asr_rule"
    private const val nlu_RULE_FILE = "asr_nlu"

    init {
        AppContextLinker.context.getExternalFilesDir(AUDIO_FILE)
        AppContextLinker.context.getExternalFilesDir(CACHE_FILE)
    }

    val CACHE_PATH = AppContextLinker.context.getExternalFilesDir(CACHE_FILE)?.absolutePath
    val AUDIO_PATH = AppContextLinker.context.getExternalFilesDir(AUDIO_FILE)?.absolutePath


    fun <T : Serializable> getCache(modelClass: Class<T>, fileName: String): Single<T> {
        return Single.create {
            it.onSuccess(ObjectInputStream(FileInputStream("$CACHE_PATH/$fileName")).readObject() as T)
        }
    }

    fun saveCache(instance: Serializable, fileName: String): Completable {
        return Completable.create {
            val fos = FileOutputStream("$CACHE_PATH/$fileName")
            val oos = ObjectOutputStream(fos)
            oos.writeObject(instance)
            oos.flush()
            oos.close()
            fos.close()
            it.onComplete()
        }
    }


}