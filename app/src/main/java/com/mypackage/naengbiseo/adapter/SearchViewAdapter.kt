package com.mypackage.naengbiseo.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.viewmodel.MainViewModel

class SearchViewAdapter(private val viewModel: MainViewModel) :
    RecyclerView.Adapter<SearchViewHolder>() {
    private val TYPE_NULL = 2
    private val TYPE_CATEGORY_HEADER = 1
    private val TYPE_ITEM = 0

    override fun getItemCount(): Int {
        //Log.d("dataSize",viewModel.getSortedData(1).size.toString())
        return viewModel.getSearchData()!!.size
    }

    override fun getItemViewType(position: Int): Int {
        var foodList: List<FoodData>? = viewModel.getSearchData()
        //Log.d("dataSize",viewModel.getColdData().size.toString())

        if(foodList!![position].Null == 1){
            return TYPE_NULL
        }
        else if (foodList[position].header == 1) {
            return TYPE_CATEGORY_HEADER
        }
        else return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val myLayout: Int = when (viewType) {
            TYPE_CATEGORY_HEADER -> R.layout.main_header
            TYPE_ITEM -> R.layout.food_item_search_version
            TYPE_NULL -> R.layout.not_found_food
            else -> R.layout.fragment_error
        }
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(myLayout, parent, false))
        /*val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(inflatedView)*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        holder.bind((viewModel.getSearchData() ?: return)[position],position)

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