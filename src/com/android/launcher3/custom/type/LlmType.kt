package com.android.launcher3.custom.type

enum class LlmType(val value: String) {
    VIRNECT_LLM("virnectLlm"),
    OPEN_AI_LLM("OpenAiLlm"),
    CHAT_X_LLM("ChatXLlm");

    companion object {
        fun fromValue(value: String): LlmType {
            return entries.find { it.value == value } ?: VIRNECT_LLM
        }
    }
}
