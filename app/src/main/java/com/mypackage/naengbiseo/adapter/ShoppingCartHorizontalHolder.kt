package com.mypackage.naengbiseo.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.FoodIcon
import kotlinx.android.synthetic.main.food_icon_item.view.*

class ShoppingCartHorizontalHolder(v: View): RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(icon: FoodIcon, position: Int) {
        // icon이 들어올때
        view.foodIconImageView.setImageResource(icon.getIconResource)
    }
}