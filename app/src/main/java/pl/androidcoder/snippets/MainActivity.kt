package pl.androidcoder.snippets

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pl.androidcoder.snippets.externalapp.ExternalAppLauncher

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openApp.setOnClickListener {
            ExternalAppLauncher("com.n7mobile.wallpaper", this)
                .launchApp()
        }

    }
}
