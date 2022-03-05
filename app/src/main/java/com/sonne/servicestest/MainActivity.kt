package com.sonne.servicestest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.sonne.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
//            для остановки сервиса: stopService
            stopService(MyForegroundService.newIntent(this))

            startService(MyService.newIntent(this, 25))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyForegroundService.newIntent(this))
        }
    }
}