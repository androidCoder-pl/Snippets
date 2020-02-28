package pl.androidcoder.snippets.externalapp

import android.content.Context
import android.content.Intent
import android.net.Uri


class ExternalAppLauncher(
    private val appPackage: String,
    private val context: Context
) {
    fun launchApp() {
        val intent = context.packageManager.getLaunchIntentForPackage(appPackage)
        if (intent == null) {
            launchGooglePlay()
        } else {
            context.startActivity(intent)
        }
    }

    private fun launchGooglePlay() {
        val marketIntent = context.packageManager.getLaunchIntentForPackage("com.android.vending")
        if (marketIntent != null) {
            marketIntent.data = Uri.parse("market://details?id=$appPackage")
            marketIntent.action = Intent.ACTION_VIEW
            context.startActivity(marketIntent)
        } else {
            launchGooglePlayWebsite()
        }


    }

    private fun launchGooglePlayWebsite() {
        val uri = Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
