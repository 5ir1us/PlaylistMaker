package com.example.playlistmaker.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.example.playlistmaker.presentation.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MediaLibraryActivity : AppCompatActivity() {
  lateinit var binding: ActivityMediaLibraryBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val viewPager: ViewPager2 = findViewById(R.id.view_pager)
    val tabLayout: TabLayout = findViewById(R.id.tab_layout)


    val adapter = ViewPagerAdapter(this)

    viewPager.adapter = adapter

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
      tab.text = when (position) {

        0 ->  getString(R.string.tab_favorite_tracks)
        1 ->  getString(R.string.tab_playlists)
        else -> ""
      }


    }.attach()

    binding.buttonBack.setOnClickListener {
      finish()
    }
  }

}

