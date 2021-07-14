package cn.darkfog.ai.symbiosis.assistant.engine.dialog.bean

import cn.darkfog.ai.symbiosis.assistant.engine.speech.bean.Slot

data class Input(
    val domain: String,
    val intent: String,
    val triggerString: String,
    val slotsKey: Set<String>,
    val slots: MutableList<Slot>
)

