package com.android.launcher3.custom.vo


data class VisionXSettingData(
    var sttType: String,
    var llmType: String,
    var ttsEnabled: Int,
    var voiceRecognitionInterval: String,
    var chatXAccount: String,
    var chatXPassword: String,
    var remoteWorkspaceId: String,
    var remoteCalleeAccounts: List<String>,
    var showingDebugView: Boolean,
)