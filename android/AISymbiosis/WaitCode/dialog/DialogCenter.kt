package cn.darkfog.ai.symbiosis.assistant.dialog

//可以用kapt优化
//跟view状态绑定的方案有思路了

object DialogCenter {

    private val localIntents = hashMapOf<String, DialogIntent>()

    fun addLocalIntent(intent: DialogIntent) {
        localIntents[intent.INTENT] = intent
    }

    fun isLocal(result: NluResult): Boolean? {
        return localIntents[result.intent]?.checkSlots(result) ?: false
    }

    fun generateRunnable(result: NluResult): Runnable? {
        return localIntents[result.intent]?.getRunnable(result)
    }
}