package com.apliecream.boardproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.apliecream.boardproject.auth.IntroActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splach)

        Handler().postDelayed({
            startActivity(Intent(this,IntroActivity::class.java))
            finish()
        }, 3000)

    }
}