package com.example.trinity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private var itemList : List<Contacts>, val _food_place : String) :RecyclerView.Adapter<ContactsViewHolder>() {
    private var food_place = _food_place
    var listOfFoodId = mutableListOf<Long>()
    var listOfDeleteFoodId = mutableListOf<Long>()

    fun setItemList(_itemList: List<Contacts>) {
        itemList = _itemList
    }

    fun clearFoodId() {
        listOfFoodId.clear()
        listOfDeleteFoodId.clear()
    }

    fun getDeleteFoodIds(): MutableList<Long> {
        return listOfDeleteFoodId
    }

    fun setDeleteIdList(listOfDeletePosition: MutableList<Int>) {
        for(position in listOfDeletePosition) {
            Log.d("MSG", "(at RecyclerViewAdapter) Delete Id: " + listOfFoodId[position])
            listOfDeleteFoodId.add(listOfFoodId[position])
        }
    }

    override fun getItemCount(): Int { // 아 이 개수만큼 아이템을 가져오는거네;;;
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder { // 한놈
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.list_foods, parent, false)
        return ContactsViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) { // 새로 들어온 뷰홀더를 묶음 - list바뀔때마다 호출되는듯
        val item = itemList[position]

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        Log.d("MSG", "(Binding item) name: " + item.name + ", id: " + item.id)
        listOfFoodId.add(item.id)
        holder.apply {
            bind(item)
        }

    }

//    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

}