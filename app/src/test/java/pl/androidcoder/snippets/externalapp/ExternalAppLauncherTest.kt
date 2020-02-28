package pl.androidcoder.snippets.externalapp

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf


@RunWith(AndroidJUnit4::class)
class ExternalAppLauncherTest {

    private val app = getApplicationContext<Application>()
    private val shadowPackageManager = shadowOf(app.packageManager)

    @Test
    fun `should open browser with google play when app and GooglePlay is not installed`() {
        //GIVEN
        val appPackage = "com.n7mobile.wallpaper"
        val externalAppLauncher = ExternalAppLauncher(appPackage, app)
        val expectedUri = Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
        //WHEN
        externalAppLauncher.launchApp()
        //THEN
        val actualIntent = shadowOf(app).nextStartedActivity
        assertThat(actualIntent.action, equalTo(Intent.ACTION_VIEW))
        assertThat(actualIntent.data, equalTo(expectedUri))
    }

    @Test
    fun `should open GooglePlay with app when app is not installed`() {
        //GIVEN
        val appPackage = "com.n7mobile.wallpaper"
        val googlePlayPackage = "com.android.vending"
        val expectedUri = Uri.parse("market://details?id=$appPackage")
        installApp(googlePlayPackage)
        val externalAppLauncher = ExternalAppLauncher(appPackage, app)
        //WHEN
        externalAppLauncher.launchApp()
        //THEN
        val actualIntent = shadowOf(app).nextStartedActivity
        assertThat(actualIntent.action, equalTo(Intent.ACTION_VIEW))
        assertThat(actualIntent.component.packageName, equalTo(googlePlayPackage))
        assertThat(actualIntent.data, equalTo(expectedUri))
    }


    @Test
    fun `should open app when app is installed`() {
        //GIVEN
        val appPackage = "com.n7mobile.wallpaper"
        val externalAppLauncher = ExternalAppLauncher(appPackage, app)
        installApp(appPackage)
        //WHEN
        externalAppLauncher.launchApp()
        //THEN
        val actualIntent = shadowOf(app).nextStartedActivity
        assertThat(actualIntent.action, equalTo(Intent.ACTION_MAIN))
        assertThat(actualIntent.component.packageName, equalTo(appPackage))
    }

    private fun installApp(appPackage: String) {
        val activityInfo = ActivityInfo()
        activityInfo.name = "LaunchActivity"
        activityInfo.packageName = appPackage

        val appPackage = ComponentName(appPackage, "LaunchActivity")
        val intentFilter = IntentFilter()
        intentFilter.addCategory(Intent.CATEGORY_LAUNCHER)
        intentFilter.addAction(Intent.ACTION_MAIN)

        shadowPackageManager.addOrUpdateActivity(activityInfo)
        shadowPackageManager.addIntentFilterForActivity(appPackage, intentFilter)
    }
}