package cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean

data class TriggerJson(
    val domain: String,
    val intents: List<String>,
    val path:String,
    val loadWhenInit:Boolean
)