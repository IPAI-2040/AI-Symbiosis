package cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean

import com.google.gson.annotations.SerializedName


data class TaskJson(
    val action_end: ActionEnd,
    val breakable: Boolean,
    val description: String,
    val engine: String,
    val name: String,
    val parameters: List<Frame>,
    val version: String
)

data class ActionEnd(
    @SerializedName("dialog-act")
    val dialogAct: DialogAct,
    val implicit_confirm: String,
    val type: String
)

data class Frame(
    @SerializedName("dialog-act")
    val dialogAct: DialogAct,
    val name: String,
    val parameters: List<String>,
    val required: String,
    val resolver: String,
    val skip: String,
    val type: String,
    @SerializedName("need_confirm")
    val needConfirm: Boolean
)

data class DialogAct(
    @SerializedName("inform_choose")
    val informChoose: String,
    @SerializedName("inform_confirm")
    val informConfirm: String,
    @SerializedName("inform_failure")
    val informFailure: String,
    @SerializedName("inform_success")
    val informSuccess: String
)