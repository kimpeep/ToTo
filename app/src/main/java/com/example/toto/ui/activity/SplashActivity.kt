package com.example.toto.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        handler.postDelayed(Runnable{
            var intent: Intent = Intent(applicationContext, MainActivity::class.java) /* MainActivity로 시작 */
            startActivity(intent)
            finish()
        }, 2000)
    }
}