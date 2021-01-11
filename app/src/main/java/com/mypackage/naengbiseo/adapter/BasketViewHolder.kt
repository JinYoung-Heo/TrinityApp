package com.mypackage.naengbiseo.adapter

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.basket_food_item.view.*

class BasketViewHolder(v: View): RecyclerView.ViewHolder(v) {
    companion object { // companion object는 JAVA로 치면 static
        private var checkbox_state = 0
        fun activateCheckbox() {
            checkbox_state = 1
        }
        fun inActivateCheckbox() {
            checkbox_state = 0
        }
    }
    var view: View = v

    fun bind(foodData: FoodData, position: Int) {
        view.foodIcon.setImageResource(foodData.foodIcon)
        view.foodNameEditText.setText(foodData.foodName)
        view.quantityTextView.setText(foodData.foodNumber.toString())
        when (foodData.storeLocation) {
            "shelf" -> {
                view.radio_btn1.isChecked = true
            }
            "cool" -> {
                view.radio_btn2.isChecked = true
            }
            "cold" -> {
                view.radio_btn3.isChecked = true
            }
            else -> {
                Toast.makeText(view.context, "radio button error", Toast.LENGTH_SHORT).show()
            }
        }
        // 체크박스 관련
        if(checkbox_state == 1) {
            view.basket_check_box.visibility = View.VISIBLE
            view.basket_check_box.setChecked(false)
            view.buyButton.visibility = View.GONE
        }
        else {
            view.basket_check_box.visibility = View.GONE
            view.buyButton.visibility = View.VISIBLE
        }
    }
}


