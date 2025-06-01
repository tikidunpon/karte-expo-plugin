package expo.modules.karte

import android.app.Application
import android.content.pm.PackageManager
import expo.modules.kotlin.AppLifecycleListener
import io.karte.android.KarteApp
import io.karte.android.core.Config
import io.karte.android.inappmessaging.InAppMessagingConfig

private const val KARTE_EDGE_TO_EDGE_ENABLED_KEY = "KARTE_EDGE_TO_EDGE_ENABLED"

class KarteApplicationLifecycleListener : AppLifecycleListener {
  override fun onCreate(application: Application) {
    KarteApp.setup(application, createKarteConfig(application))
  }

  /**
   * Returns config if the manifest contains [KARTE_EDGE_TO_EDGE_ENABLED_KEY], otherwise returns null so the SDK
   * initializes with default settings.
   */
  private fun createKarteConfig(app: Application): Config? {
    val meta = app.packageManager
      .getApplicationInfo(app.packageName, PackageManager.GET_META_DATA)
      .metaData
      ?: return null

    if (!meta.containsKey(KARTE_EDGE_TO_EDGE_ENABLED_KEY)) return null

    val enabled = meta.getBoolean(KARTE_EDGE_TO_EDGE_ENABLED_KEY, false)
    val iamConfig = InAppMessagingConfig.Builder()
      .isEdgeToEdgeEnabled(enabled)
      .build()

    return Config.Builder()
      .libraryConfigs(iamConfig)
      .build()
  }
}
