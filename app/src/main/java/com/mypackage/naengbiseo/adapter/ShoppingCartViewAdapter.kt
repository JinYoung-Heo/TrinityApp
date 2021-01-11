package com.mypackage.naengbiseo.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.FoodIcon
import com.mypackage.naengbiseo.R

class ShoppingCartViewAdapter(var iconList: MutableList<FoodIcon>): RecyclerView.Adapter<ShoppingCartViewHolder>() {
    private val TYPE_NO_ICON = -1
    var selectList= mutableListOf<Boolean>()
    private val TYPE_CATEGORY_HEADER = 0
    private val TYPE_CATEGORY_FOOTER = 1
    private val TYPE_ITEM = 2

    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (iconList[position].getIconResource == 0) {
            return TYPE_CATEGORY_HEADER
        }
        else if (iconList[position].getIconName == "footer") {
            return TYPE_CATEGORY_FOOTER
        }
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        for(i in 0..iconList.size-1){
            selectList.add(false)
        }
        var myLayout: Int
        when(viewType) {
            TYPE_CATEGORY_HEADER -> myLayout = R.layout.food_category_header_item
            TYPE_CATEGORY_FOOTER -> myLayout = R.layout.food_category_footer_item
            TYPE_ITEM -> myLayout = R.layout.food_icon_item
            else -> myLayout = R.layout.fragment_error
        }
        return ShoppingCartViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val icon = iconList[position]
        holder.bind(icon, position)
//        Log.d("MSG", "iconList size: " + iconList.size.toString() + ", icon name: " + icon.getIconName)
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