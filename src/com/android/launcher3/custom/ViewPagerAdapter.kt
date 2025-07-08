package com.android.launcher3.custom

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.android.launcher3.R
import com.android.launcher3.custom.type.LlmType
import com.android.launcher3.custom.type.SttType
import com.android.launcher3.databinding.VisionAiSettingViewBinding

class ViewPagerAdapter(
    private val context: Context,
    private val listData: ArrayList<PageInfo>,
    private val visionAiCommand: List<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TAG = "LauncherViewPagerAdapter"

    private var chatXAccount:String = ""
    private var chatXPassword:String = ""
    private var remoteCallee:String = ""
    private var remoteWorkspaceId:String = ""
    private var voiceRecognitionInterval:String = ""

    override fun getItemViewType(position: Int): Int {
        return listData[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        if (viewType == VIEW_TYPE_VISION_AI_MAIN_VIEW) {
            val view = inflater.inflate(R.layout.vision_ai_main_view, parent, false)
            return ViewHolderTypeOne(view)
        } else {
            val binding = VisionAiSettingViewBinding.inflate(inflater,parent,false)
            return ViewHolderTypeTwo(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("by_debug", "onBindViewHolder $position ")
        if (holder is ViewHolderTypeOne) {
            for (s in visionAiCommand) {
                if (!s.isEmpty()) {
                    val textView = TextView(context)
                    textView.id = View.generateViewId()

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 10, 0, 10)
                    textView.layoutParams = params

                    textView.setPadding(0, 0, 0, 0)
                    textView.gravity = Gravity.START
                    textView.setTextColor(Color.WHITE)
                    textView.includeFontPadding = false

                    if (Build.MODEL == "CK01") {
                        textView.textSize = 20f
                    } else {
                        textView.textSize = 30f
                    }

                    textView.text = s
                    holder.commandContainer.addView(textView)
                }
            }
        }else if(holder is ViewHolderTypeTwo){
            initSettingView(holder.binding)
        }
    }

    fun initSettingView(binding: VisionAiSettingViewBinding){
        binding.btnAudioSettings.setOnTouchListener { view, motionEvent ->
            context.sendBroadcast(Intent("com.virnect.SHOW_MANUAL_MIC_SETTING").apply {
                setPackage("com.virnect.apps.visionx")
            })
            false
        }

        binding.rgSttType.check(
            when(VisionXSettingHelper.loadedVisionXSettingData.sttType){
                SttType.REMOTE_WHISPER_SYNC.value -> R.id.rb_whisper_sync_stt
                SttType.REMOTE_GOOGLE_STREAMING.value -> R.id.rb_google_streaming_stt
                SttType.LOCAL_WHISPER_CPP.value -> R.id.rb_whisper_cpp_stt
                else -> R.id.rb_whisper_sync_stt
            }
        )

        binding.rgLlmType.check(
            when(VisionXSettingHelper.loadedVisionXSettingData.llmType){
                LlmType.VIRNECT_LLM.value -> R.id.rb_virnect_llm
                LlmType.OPEN_AI_LLM.value -> R.id.rb_open_ai_llm
                LlmType.CHAT_X_LLM.value -> R.id.rb_chat_x_llm
                else -> R.id.rb_virnect_llm
            }
        )

        binding.rgTtsView.check(
            when(VisionXSettingHelper.loadedVisionXSettingData.ttsEnabled){
                0 -> R.id.rb_tts_on
                else -> R.id.rb_tts_off
            }
        )

        binding.rgAudioLogView.check(
            if(VisionXSettingHelper.loadedVisionXSettingData.showingDebugView){
                R.id.rb_visible
            }else{
                R.id.rb_gone
            }
        )

        binding.rgSttType.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "stt type changed, checkedId=$checkedId")
            if (checkedId == R.id.rb_whisper_sync_stt) {
                VisionXSettingHelper.updateVisionXSettingData(context, sttType = SttType.REMOTE_WHISPER_SYNC.value)
            } else if (checkedId == R.id.rb_google_streaming_stt) {
                VisionXSettingHelper.updateVisionXSettingData(context, sttType = SttType.REMOTE_GOOGLE_STREAMING.value)
            } else {
                VisionXSettingHelper.updateVisionXSettingData(context, sttType = SttType.LOCAL_WHISPER_CPP.value)
            }
        }

        binding.rgLlmType.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "llm type changed, checkedId=$checkedId")
            if (checkedId == R.id.rb_virnect_llm) {
                VisionXSettingHelper.updateVisionXSettingData(context, llmType = LlmType.VIRNECT_LLM.value)
            } else if (checkedId == R.id.rb_open_ai_llm) {
                VisionXSettingHelper.updateVisionXSettingData(context, llmType = LlmType.OPEN_AI_LLM.value)
            } else if (checkedId == R.id.rb_chat_x_llm) {
                VisionXSettingHelper.updateVisionXSettingData(context, llmType = LlmType.CHAT_X_LLM.value)
            }
        }

        binding.rgTtsView.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "tts view Type changed, checkedId=$checkedId")
            if (checkedId == R.id.rb_tts_on) {
                VisionXSettingHelper.updateVisionXSettingData(context, ttsEnabled = 0)
            } else if (checkedId == R.id.rb_tts_off) {
                VisionXSettingHelper.updateVisionXSettingData(context, ttsEnabled = 1)
            }
        }

        binding.etVoiceInterval.addTextChangedListener {
            Log.d("by_debug", "etVoiceInterval: $it ")
            voiceRecognitionInterval = it?.toString() ?: ""
        }

        binding.etChatxId.addTextChangedListener {
            Log.d("by_debug", "chatXAccount: $it ")
            chatXAccount = it?.toString() ?: ""
        }

        binding.etChatxPwd.addTextChangedListener {
            Log.d("by_debug", "chatXPassword: $it ")
            chatXPassword = it?.toString() ?: ""
        }

        binding.etRemoteCalleeId.addTextChangedListener {
            Log.d("by_debug", "remoteCallee: $it ")
            remoteCallee = it?.toString() ?: ""
        }

        binding.etRemoteWorkspaceId.addTextChangedListener {
            Log.d("by_debug", "remoteWorkspaceId: $it ")
            remoteWorkspaceId = it?.toString() ?: ""
        }

        VisionXSettingHelper.loadedVisionXSettingData.run {
            if(voiceRecognitionInterval.isEmpty()){
                binding.etVoiceInterval.setText("1200")
            }else{
                binding.etVoiceInterval.setText(voiceRecognitionInterval)
            }

            if(chatXAccount.isEmpty()){
                binding.etChatxId.setText("msuser1@model-solution.com")
            }else{
                binding.etChatxId.setText(chatXAccount)
            }

            if(chatXPassword.isEmpty()){
                binding.etChatxPwd.setText("12345678")
            }else{
                binding.etChatxPwd.setText(chatXPassword)
            }

            Log.d("by_debug", "remoteCalleeAccounts : $remoteCalleeAccounts")
            Log.d("by_debug", "isEmpty : ${remoteCalleeAccounts.size}")
            if(remoteCalleeAccounts.isEmpty()){
                binding.etRemoteCalleeId.setText("msuser2,msuser3,msuser4")
            }else{
                binding.etRemoteCalleeId.setText(remoteCalleeAccounts.joinToString(","))
            }

            if(remoteWorkspaceId.isEmpty()){
                binding.etRemoteWorkspaceId.setText("6323b3b136de407193d4f7a2cceaf50d")
            }else{
                binding.etRemoteWorkspaceId.setText(remoteWorkspaceId)
            }
        }
    }

    fun savedVisionXSettingData(){
        VisionXSettingHelper.updateVisionXSettingData(context,
            chatXAccount = chatXAccount,
            chatXPassword = chatXPassword,
            remoteWorkspaceId = remoteWorkspaceId,
            remoteCalleeAccounts = remoteCallee.split(","),
            voiceRecognitionInterval = voiceRecognitionInterval)
    }

    override fun getItemCount(): Int {
        return listData.size // 총 2페이지
    }

    // ViewHolder for Type One
    internal class ViewHolderTypeOne(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var commandContainer: LinearLayout =
            itemView.findViewById(R.id.ll_voice_command_container)
    }


    // ViewHolder for Type Two
    internal class ViewHolderTypeTwo(val binding: VisionAiSettingViewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val VIEW_TYPE_VISION_AI_MAIN_VIEW: Int = 38
        const val VIEW_TYPE_VISION_AI_SETTING_VIEW: Int = 39
    }
}
