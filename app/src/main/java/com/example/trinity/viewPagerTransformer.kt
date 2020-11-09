package com.example.trinity

import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2

class viewPagerTransformer : ViewPager2.PageTransformer{
    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> { // [-Infinity,-1)
                // This page is way off-screen to the left.
                Log.d("MSG", "Page transformed")
            }
            position <= 1 -> { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                Log.d("MSG", "Page transformed")
            }
            position <= 2 -> { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                Log.d("MSG", "Page transformed")
            }
            else -> { // (1,+Infinity]
                // This page is way off-screen to the right.
                Log.d("MSG", "Page transformed")
            }
        }
    }
}