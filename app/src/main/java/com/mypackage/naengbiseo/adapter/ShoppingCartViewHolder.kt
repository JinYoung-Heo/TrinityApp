package com.mypackage.naengbiseo.adapter

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.FoodIcon
import kotlinx.android.synthetic.main.food_category_header_item.view.*
import kotlinx.android.synthetic.main.food_icon_item.view.*

class ShoppingCartViewHolder(v: View):RecyclerView.ViewHolder(v) {
    companion object { // companion object는 JAVA로 치면 static
        private var select_state = 0
        fun selecting() {
            select_state = 1
        }
        fun canceling() {
            select_state = 0
        }
    }
    var view : View = v

    private val TYPE_CATEGORY_HEADER = 0

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun bind(icon: FoodIcon, position: Int) {
        // header가 들어올때
        if (icon.getIconResource == TYPE_CATEGORY_HEADER) {
            view.foodCategoryName.setText(icon.getIconName)
        }
        // icon이 들어올때
        else if (icon.getIconName != "footer"){
            view.foodIconImageView.setImageResource(icon.getIconResource)
            view.foodIconTextView.setText(icon.getIconName)
        }

        if(select_state == 1) {
            view.elevation=10F
        }
        else {
            view.elevation=0F
        }
    }


}