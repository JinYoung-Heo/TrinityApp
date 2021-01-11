package com.mypackage.naengbiseo.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.viewmodel.AlarmViewModel

class AlarmViewAdapter(private val viewModel: AlarmViewModel) :
    RecyclerView.Adapter<AlarmViewHolder>() {

    override fun getItemCount(): Int {
        return viewModel.alarmFoodList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.food_item_search_version, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(viewModel.getAlarmFoodAt(position).first, viewModel.getAlarmFoodAt(position).second)
    }
}