package com.android.launcher3.custom

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * An object which has a group of utility functions for [SharedPreferences].
 *
 * History
 *```
 * |---------------|------------------|-------------------------------------------------------------------------------------------------|
 * | Modified date | Editor           | Description                                                                                     |
 * |---------------|------------------|-------------------------------------------------------------------------------------------------|
 * | 2020-05-07    | Jim, Park        | Created.                                                                                        |
 * |---------------|------------------|-------------------------------------------------------------------------------------------------|
 * ```
 *
 * @author Jim, Park
 * @since 1.0
 */
class SharedPrefsHelper {
    companion object {
        /**
         * TAG
         */
        private const val TAG = "SharedPrefsHelper"

        /**
         * Default shared preferences name
         */
        private const val SHARED_PREFS_NAME = "remote"

        /**
         * Instance
         */
        private var INSTANCE: SharedPrefsHelper? = null

        /**
         * Returns the single instance of this class.
         */
        @Synchronized
        @JvmStatic
        fun getInstance() = INSTANCE
                ?: SharedPrefsHelper().also { INSTANCE = it }
    }

    /**
     * Shared preferences
     */
    private var sharedPreferences: SharedPreferences? = null

    /**
     * Editor
     */
    private var editor: SharedPreferences.Editor? = null

    /**
     * Determine if [sharedPreferences] instance is null and initialize it.
     * @param context It is referenced when using it to prevent memory leaks.
     */
    private fun getEditor(context: Context): SharedPreferences.Editor? {
        if (sharedPreferences == null)
            sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).also { editor = it.edit() }
        return editor
    }

    /**
     * Put the [value] with [key] to [sharedPreferences].
     * @param context
     * @param key the key to getting value later.
     * @param value the value that you want to save.
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    fun put(context: Context, key: String, value: Any?) = putImpl(context, key, value)

    @Throws(RuntimeException::class)
    private fun putImpl(context: Context, key: String, value: Any?) =
            getEditor(context)?.let {
                when (value) {
                    is Boolean -> it.putBoolean(key, value)
                    is Int -> it.putInt(key, value)
                    is Float -> it.putFloat(key, value)
                    is Long -> it.putLong(key, value)
                    is String -> it.putString(key, value)
                    is Set<*> -> {
                        if (value.all { data -> data is String }) {
                            @Suppress("UNCHECKED_CAST")
                            it.putStringSet(key, value as Set<String>)
                        } else {
                            throw RuntimeException("Only Set<String> is supported.")
                        }
                    }
                    else -> throw RuntimeException("Not supported value.")
                }
                it.commit()
            }

    /**
     * Get the value of [key]. It can be null.
     * @param context
     * @param key the key to get value.
     * @return value stored with [key]
     */
    fun <T> get(context: Context, key: String) = getEditor(context)?.let { sharedPreferences!!.all[key] as T }

    /**
     * Get the value of [key]. If it is null, it returns [defValue].
     * @param context
     * @param key the key to get value.
     * @param defValue the value that is returned when the value is null.
     * @return value stored with [key]
     */

    fun <T> get(context: Context, key: String, defValue: T) = getEditor(context)?.let { sharedPreferences!!.all[key] as T }
            ?: defValue

    /**
     * Delete all values ​​in [sharedPreferences].
     * @param context
     */
    fun clear(context: Context) = getEditor(context)?.clear()?.commit()

    /**
     * Delete the single value of [key].
     * @param context
     * @param key the key to remove the value.
     */
    fun delete(context: Context, key: String) = getEditor(context)?.apply { sharedPreferences!!.contains(key).let { if (it) this.remove(key).commit() } }

}
