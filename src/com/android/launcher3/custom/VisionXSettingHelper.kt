package com.android.launcher3.custom

import android.content.Context
import com.android.launcher3.custom.type.LlmType
import com.android.launcher3.custom.type.SttType
import com.android.launcher3.custom.vo.VisionXSettingData
import com.google.gson.Gson

object VisionXSettingHelper {

    val keySttType:String = "sttType"
    val keyLmmType:String = "llmType"
    val keyTtsEnabled:String = "ttsEnabled"
    val keyVoiceRecognitionInterval:String = "voiceRecognitionInterval"
    val keyChatXAccount:String = "chatXAccount"
    val keyChatXPassword:String = "chatXPassword"
    val keyRemoteWorkspaceId:String = "remoteWorkspaceId"
    val keyRemoteCalleeAccounts:String = "remoteCalleeAccounts"
    val keyShowingDebugView:String = "showingDebugView"

    lateinit var loadedVisionXSettingData:VisionXSettingData

    fun visionXSettingDataToJsonString():String{
        return Gson().toJson(loadedVisionXSettingData)
    }

    fun visionXSettingDataFromJsonString(jsonStr:String):VisionXSettingData{
        return Gson().fromJson(jsonStr,VisionXSettingData::class.java)
    }

    fun saveVisionXSettingData(context: Context,data:VisionXSettingData){
        SharedPrefsHelper.getInstance().apply {
            put(context, keySttType, data.sttType)
            put(context, keyLmmType, data.llmType)
            put(context, keyTtsEnabled, data.ttsEnabled)
            put(context, keyVoiceRecognitionInterval, data.voiceRecognitionInterval)
            put(context, keyChatXAccount, data.chatXAccount)
            put(context, keyChatXPassword, data.chatXPassword)
            put(context, keyRemoteWorkspaceId, data.remoteWorkspaceId)
            put(context, keyRemoteCalleeAccounts, data.remoteCalleeAccounts.toSet())
            put(context, keyShowingDebugView, data.showingDebugView)
        }
    }

    fun readVisionXSettingData(context:Context):VisionXSettingData?{
        var result: VisionXSettingData?

        SharedPrefsHelper.getInstance().run {
            result = VisionXSettingData(
                get(context,keySttType) ?: SttType.REMOTE_WHISPER_SYNC.value,
                get<String>(context,keyLmmType) ?: LlmType.VIRNECT_LLM.value,
                get<Int>(context, keyTtsEnabled) ?: 0,
                get<String>(context,keyVoiceRecognitionInterval) ?: "1200",
                get<String>(context,keyChatXAccount) ?: "msuser1@model-solution.com",
                get<String>(context,keyChatXPassword) ?: "12345678",
                get<String>(context,keyRemoteWorkspaceId) ?: "6323b3b136de407193d4f7a2cceaf50d",
                get<Set<String>>(context,keyRemoteCalleeAccounts)?.toList() ?: listOf("msuser2","msuser3","msuser4"),
                get<Boolean>(context,keyShowingDebugView) ?: false,
            )
        }

        result?.let {
            loadedVisionXSettingData = it
        }
        return result
    }

    fun getVisionXSettingData():VisionXSettingData?{
        if(!::loadedVisionXSettingData.isInitialized){
            return null
        }
        return loadedVisionXSettingData
    }


    fun updateVisionXSettingData(
        context: Context,
        sttType: String? = null,
        llmType: String? = null,
        ttsEnabled: Int? = null,
        voiceRecognitionInterval: String? = null,
        chatXAccount: String? = null,
        chatXPassword: String? = null,
        remoteWorkspaceId: String? = null,
        remoteCalleeAccounts: List<String>? = null,
        showingDebugView: Boolean? = null,
    ) {
        loadedVisionXSettingData.sttType = sttType ?: loadedVisionXSettingData.sttType
        loadedVisionXSettingData.llmType = llmType ?: loadedVisionXSettingData.llmType
        loadedVisionXSettingData.ttsEnabled = ttsEnabled ?: loadedVisionXSettingData.ttsEnabled
        loadedVisionXSettingData.voiceRecognitionInterval = voiceRecognitionInterval ?: loadedVisionXSettingData.voiceRecognitionInterval
        loadedVisionXSettingData.chatXAccount = chatXAccount ?: loadedVisionXSettingData.chatXAccount
        loadedVisionXSettingData.chatXPassword = chatXPassword ?: loadedVisionXSettingData.chatXPassword
        loadedVisionXSettingData.remoteWorkspaceId = remoteWorkspaceId ?: loadedVisionXSettingData.remoteWorkspaceId
        loadedVisionXSettingData.remoteCalleeAccounts = remoteCalleeAccounts ?: loadedVisionXSettingData.remoteCalleeAccounts
        loadedVisionXSettingData.showingDebugView = showingDebugView ?: loadedVisionXSettingData.showingDebugView

        saveVisionXSettingData(context,loadedVisionXSettingData)
    }
}


