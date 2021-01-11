package com.mypackage.naengbiseo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.FoodIcon
import com.mypackage.naengbiseo.R

class ShoppingCartHorizontalAdapter(var iconList: MutableList<FoodIcon>): RecyclerView.Adapter<ShoppingCartHorizontalHolder>() {
    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartHorizontalHolder {
        return ShoppingCartHorizontalHolder(LayoutInflater.from(parent.context).inflate(R.layout.horizontal_icon_item, parent, false))
    }

    override fun onBindViewHolder(holder: ShoppingCartHorizontalHolder, position: Int) {
        val icon = iconList[position]
        holder.bind(icon, position)

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

    }

    //    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}