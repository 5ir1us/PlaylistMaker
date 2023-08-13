package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.searchcid)
        val mediaButton = findViewById<Button>(R.id.mediaLibraryId)
        val sittingsButton = findViewById<Button>(R.id.sittingId)


        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchcActivity::class.java)
            startActivity(searchIntent)
        }
        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaIntent)

        }

        sittingsButton.setOnClickListener {
            val settingIntent = Intent (this, SettingsActivity::class.java)
            startActivity(settingIntent)

        }
    }
}