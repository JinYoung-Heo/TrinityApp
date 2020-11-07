package com.example.trinity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_viewpager_refrigerator.*
import kotlinx.android.synthetic.main.fragment_viewpager_shelf.*

class ViewPagerShelfFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewpager_shelf, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val main_activity = activity as MainActivity
        val adapter = main_activity.getAdapter(0)
        shelf_recycler_view.adapter = adapter
        Log.d("MSG", "Shelf adapter attached")
        Log.d("MSG", "Adapter's place is " + adapter.getFoodPlaceName())
    }
}