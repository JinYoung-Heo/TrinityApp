package com.example.trinity

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.PagerAdapter.POSITION_NONE
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }


    override fun createFragment(position: Int): Fragment { // 뷰페이저가 만들어질때 한번 호출됨

        return when(position) {
            0 -> {
                Log.d("MSG", "Shelf view page opened")
                ViewPagerShelfFragment()
            }
            1 -> {
                Log.d("MSG", "Refrigerator view page opened")
                ViewPagerRefrigeratorFragment()
            }
            2 -> {
                Log.d("MSG", "Freezer view page opened")
                ViewPagerFreezerFragment()
            }
            else -> ViewPagerErrorFragment()
        }
    }

}