package com.redcatgames.musiclib.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redcatgames.musiclib.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = binding.root
        setContentView(view)

        //binding.text1.text = BuildConfig.APPLICATION_ID
    }
}