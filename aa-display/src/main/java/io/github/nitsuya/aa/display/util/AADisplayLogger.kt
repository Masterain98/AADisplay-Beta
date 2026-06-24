package io.github.nitsuya.aa.display.util

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * In-app log capture for AADisplay diagnostics.
 * Captures key events to an in-memory ring buffer and optionally writes to a file.
 * Can be toggled via Settings > Debug Input Injection Log.
 */
object AADisplayLogger {
    private const val TAG = "AADisplay_Logger"
    private const val MAX_ENTRIES = 2000

    private val buffer = ConcurrentLinkedQueue<String>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    @Volatile
    var enabled = false

    /**
     * Log a diagnostic message. Only captured when [enabled] is true.
     */
    fun log(tag: String, msg: String) {
        if (!enabled) return
        val entry = "${dateFormat.format(Date())} [$tag] $msg"
        buffer.offer(entry)
        // Trim to max size
        while (buffer.size > MAX_ENTRIES) {
            buffer.poll()
        }
    }

    /**
     * Log with throwable.
     */
    fun log(tag: String, msg: String, t: Throwable) {
        log(tag, "$msg: ${t.message}\n${Log.getStackTraceString(t)}")
    }

    /**
     * Get all captured log lines.
     */
    fun getLogs(): List<String> = buffer.toList()

    /**
     * Export logs to a file in the app's external files directory.
     * Returns the file path, or null on failure.
     */
    fun exportToFile(context: Context): String? {
        return try {
            val dir = File(context.getExternalFilesDir(null), "logs")
            dir.mkdirs()
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val file = File(dir, "aadisplay_log_$timestamp.txt")
            file.writeText(buildString {
                appendLine("=== AADisplay Diagnostic Log ===")
                appendLine("Exported: ${dateFormat.format(Date())}")
                appendLine("Device: ${android.os.Build.DEVICE} (${android.os.Build.MODEL})")
                appendLine("Android: ${android.os.Build.VERSION.RELEASE} (API ${android.os.Build.VERSION.SDK_INT})")
                appendLine("Entries: ${buffer.size}")
                appendLine("================================")
                appendLine()
                buffer.forEach { appendLine(it) }
            })
            Log.i(TAG, "Log exported to: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to export log", e)
            null
        }
    }

    /**
     * Clear all captured logs.
     */
    fun clear() {
        buffer.clear()
    }
}
