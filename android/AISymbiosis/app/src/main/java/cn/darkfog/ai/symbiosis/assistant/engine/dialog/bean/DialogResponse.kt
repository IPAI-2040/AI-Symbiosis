package cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean

import com.google.gson.JsonObject

data class DialogResponse(
    val status: TaskStatus,
    val response: String,
    val data:JsonObject
)