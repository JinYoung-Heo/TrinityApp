package com.example.trinity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var db : AppDatabase? = null
    private var foodList : MutableList<Contacts> = mutableListOf<Contacts>()
    private var adapterList : MutableList<RecyclerViewAdapter> = mutableListOf()
    private val foodPlaceList = listOf("shelf", "refrigerator", "freezer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.host_activity)

        //초기화
        db = AppDatabase.getInstance(this)

        setFoodListByDB()
        getFoodsAndMakeAdapters()
    }
    fun setFoodListByDB() {
        //이전에 저장한 내용 모두 불러와서 추가하기
        val savedContacts = db!!.contactsDao().getAll()
        if (foodList.size != 0) {
            foodList.clear()
        }
        if (savedContacts.isNotEmpty()) {
            foodList.addAll(savedContacts)
        }
    }
    fun getFoodsAndMakeAdapters() {
        var foods: MutableList<Contacts>
        for(foodPlace in foodPlaceList) {
            foods = getFood(foodList, foodPlace)
            adapterList.add(RecyclerViewAdapter(foods, foodPlace))
        }
    }

    fun getFood(foodList: MutableList<Contacts>, foodPlace: String): MutableList<Contacts> {
        val foods = mutableListOf<Contacts>()
        for(food in foodList) {
            if(food.location == foodPlace) foods.add(food)
        }
        return foods
    }

    fun addDB(contact : Contacts) {
        db?.contactsDao()?.insertAll(contact) //DB에 추가
    }

    fun addListToAdapter(place: String, index: Int) {
        setFoodListByDB()
        val foods = getFood(foodList, place)
        adapterList[index] = RecyclerViewAdapter(foods, place)
    }

    fun removeDB() {
        val deleteFoodIds = mutableListOf<Long>()
        for(i in 0..2) {
            for(foodId in adapterList[i].getDeleteFoodIds()) {
                deleteFoodIds.add(foodId)
            }
        }
        if(deleteFoodIds.size > 0) {
            for(id in deleteFoodIds) {
                for(food in foodList) {
                    if (id == food.id) {
                        db?.contactsDao()?.delete(contacts = food) //DB에서 삭제
                        break
                    }
                }
            }
        }
        setFoodListByDB()
        var foods: MutableList<Contacts>
        for(i in 0..2) {
            foods = getFood(foodList, foodPlaceList[i])
            adapterList[i].setItemList(foods)
        }
    }

    fun getAdapter(place: Int): RecyclerViewAdapter {
        return adapterList[place]
    }

    fun getAdapterList(): MutableList<RecyclerViewAdapter> {
        return adapterList
    }
}