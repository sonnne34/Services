package com.sonne.servicestest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sonne.servicestest.databinding.ActivityMainBinding
import ru.sumin.servicestest.MyService

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            startService(MyService.newIntent(this, 25))
        }

    }
}