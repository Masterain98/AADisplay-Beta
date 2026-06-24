package io.github.nitsuya.aa.display

import android.content.Context
import android.os.Process
import com.google.android.material.color.DynamicColors
import com.topjohnwu.superuser.Shell
import io.github.nitsuya.aa.display.util.AADisplayConfig
import io.github.nitsuya.aa.display.util.XposedConfigSync
import io.github.nitsuya.aa.display.util.tryOrNull
import io.github.nitsuya.aa.display.xposed.CoreManagerService
import io.github.nitsuya.aa.display.xposed.CoreManager
import io.github.nitsuya.aa.display.xposed.ModuleResourceBridge


val IsSystemEnv by lazy {
    Process.myUid() == 1000
}
val CoreApi by lazy {
    if(!IsSystemEnv) CoreManager
    else CoreManagerService.instance!!
}
lateinit var App : Application
class Application: android.app.Application() {
    companion object {
        private const val MIGRATION_SCREEN_OFF_DEFAULT_TRUE =
            "Migration_ScreenOffReplaceLockScreen_DefaultTrue_v1"
    }

    init {
        App = this
        tryOrNull {
            Shell.setDefaultBuilder(Shell.Builder.create().setTimeout(30))
        }
    }

    override fun onCreate() {
        super.onCreate()
        migrateScreenOffDefault(this)
        ModuleResourceBridge.bind(applicationInfo)
        DynamicColors.applyToActivitiesIfAvailable(this)
        XposedConfigSync.init(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    /**
     * One-time migration: default ScreenOffReplaceLockScreen to true for existing users
     * who haven't explicitly set the preference. This ensures the safe default is applied
     * so that Android Auto input continues working after the phone screen is turned off.
     */
    private fun migrateScreenOffDefault(context: Context) {
        val prefs = context.getSharedPreferences(AADisplayConfig.ConfigName, Context.MODE_PRIVATE)
        if (prefs.getBoolean(MIGRATION_SCREEN_OFF_DEFAULT_TRUE, false)) return

        prefs.edit().apply {
            if (!prefs.contains(AADisplayConfig.ScreenOffReplaceLockScreen.key)) {
                putBoolean(AADisplayConfig.ScreenOffReplaceLockScreen.key, true)
            }
            putBoolean(MIGRATION_SCREEN_OFF_DEFAULT_TRUE, true)
        }.apply()
    }
}
