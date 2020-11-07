package com.example.trinity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trinity.ContactsViewHolder

class RecyclerViewAdapter(private val itemList : List<Contacts>, val _food_place : String) : RecyclerView.Adapter<ContactsViewHolder>()  {
    private var food_place = _food_place
    var listOfFoodId = mutableListOf<Long>()
    var listOfDeleteFoodId = mutableListOf<Long>()
    fun getDeleteFoodIds(): MutableList<Long> {
        return listOfDeleteFoodId
    }
    fun setDeleteIdList(listOfDeletePosition : MutableList<Int>) {
        for(position in listOfDeletePosition) {
            listOfDeleteFoodId.add(listOfFoodId[position])
        }
    }
    fun getFoodPlaceName() : String {
        return food_place
    }
    override fun getItemCount(): Int { // 아 이 개수만큼 아이템을 가져오는거네;;;
        listOfFoodId.clear()
        listOfDeleteFoodId.clear()
        return getItemCount2(itemList)
    }
    private fun getItemCount2(itemList : List<Contacts>) : Int {
        var count = 0
        for(item in itemList) {
            if(item.location == food_place) count++
        }
        return count
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

        if(item.location == food_place) { // 얘가 한번만 호출되나?ㄴㄴ position이 의도대로 안되는듯 TODO position이 뭔지 알아봐
            listOfFoodId.add(item.id)
            holder.apply {
                bind(item)
            }
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