package com.android.launcher3.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class VisionAiCommandBroadcastReceiver(private val listener:CommandReceivedListener) : BroadcastReceiver() {

    private val commands =  mutableListOf<String>()

    override fun onReceive(context: Context, intent: Intent) {
        if ("com.virnect.ACTION_SEND_VISION_AI_COMMAND" == intent.action) {
            Log.i("by_debug", "런처 브로드캐스트 받음!")
            val commands = intent.getStringArrayExtra("command")
            commands?.forEach {
                Log.i("by_debug", "command = $it")
                this.commands.add(it)
            }
            listener.onReceived(commands?.toList() ?: listOf());
        }
    }
}