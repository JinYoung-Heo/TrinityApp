package com.example.trinity

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.list_foods.view.*

class ContactsViewHolder(v: View) : RecyclerView.ViewHolder(v){
    companion object {
        private var checkbox_state = 0
        fun activateCheckbox() {
            checkbox_state = 1
        }
        fun inActivateCheckbox() {
            checkbox_state = 0
        }
    }
    var view : View = v

    fun bind(item: Contacts) {
        view.food_name.text = item.name
        if(checkbox_state == 1) {
            view.check_box.visibility = View.VISIBLE
            view.check_box.setChecked(false)
        }
        else {
            view.check_box.visibility = View.GONE
        }
    }
}