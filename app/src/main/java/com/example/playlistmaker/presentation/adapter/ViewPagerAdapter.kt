package com.example.playlistmaker.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.ui.fragment.FavoritesFragment
import com.example.playlistmaker.presentation.ui.fragment.PlaylistsFragment

class ViewPagerAdapter(
  fragmentActivity: FragmentActivity,

  ) : FragmentStateAdapter(fragmentActivity) {

  override fun getItemCount(): Int = 2

  override fun createFragment(position: Int): Fragment {
    return when (position) {
      0 -> FavoritesFragment.newInstance()
      1 -> PlaylistsFragment.newInstance()
      else -> PlaylistsFragment.newInstance()

    }

  }


}