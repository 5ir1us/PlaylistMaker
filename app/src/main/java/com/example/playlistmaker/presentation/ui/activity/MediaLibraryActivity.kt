package com.example.playlistmaker.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {
    lateinit var binding: ActivityMediaLibraryBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)





    }

}

