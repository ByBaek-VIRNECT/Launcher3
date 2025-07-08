package com.android.launcher3.custom.type

enum class SttType(val value: String) {
    LOCAL_DEVICE("localDevice"),
    LOCAL_WHISPER_CPP("localWhisperCpp"),
    REMOTE_WHISPER_SYNC("remoteWhisperSync"),
    REMOTE_GOOGLE_STREAMING("remoteGoogleStreaming");

    companion object {
        fun fromValue(value: String): SttType {
            return entries.find { it.value == value } ?: LOCAL_DEVICE
        }
    }
}
