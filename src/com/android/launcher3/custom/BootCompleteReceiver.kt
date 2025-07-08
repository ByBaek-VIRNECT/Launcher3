package com.android.launcher3.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Boot Completed Receiver
 *
 * History
 *```
 * |---------------|------------------|-------------------------------------------------------------------------------------------------|
 * | Modified date | Editor           | Description                                                                                     |
 * |---------------|------------------|-------------------------------------------------------------------------------------------------|
 * | 2021-05-12    | Beomyeol,Baek    | Created.                                                                                        |
 * |---------------|------------------|-------------------------------------------------------------------------------------------------|
 * ```
 * @author Beomyeol,Baek
 * @since 1.0
 */
class BootCompleteReceiver : BroadcastReceiver(){

    /**
     * Context
     */
    private var context: Context? = null


    /**
     * onReceive
     *
     * @param context
     * @param intent
     */
    override fun onReceive(context: Context, intent: Intent?) {
        this.context = context
        if (intent == null || intent.action == null)
            return

        Log.d("by_debug", "onReceive, action = ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val i = Intent("com.virnect.START_DEVICE_CONTROL_SERVICE")
            i.setPackage("com.virnect.apps.visionx")
            context.sendBroadcast(i)
        }
    }
}