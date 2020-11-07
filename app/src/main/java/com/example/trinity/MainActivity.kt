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

        //이전에 저장한 내용 모두 불러와서 추가하기
        val savedContacts = db!!.contactsDao().getAll()
        if (savedContacts.isNotEmpty()) {
                foodList.addAll(savedContacts)
        }

        for(foodPlace in foodPlaceList) {
            foodList = sortingFood(foodList, foodPlace)
            adapterList.add(RecyclerViewAdapter(foodList, foodPlace))
        }
    }

    fun sortingFood(foodList : MutableList<Contacts>, foodPlace : String) : MutableList<Contacts> {
        val foodList = foodList.sortedWith(Comparator<Contacts>{ a, b ->
            when {
                a.location == foodPlace -> -1
                else -> 1
            }
        }).toMutableList()
        return foodList
    }

    fun addDB(contact : Contacts) {
        db?.contactsDao()?.insertAll(contact) //DB에 추가
    }

    fun getDB(place: Int) : List<Contacts> {
        return db!!.contactsDao().getAll()
    }

    fun addList(contact : Contacts, place: String, index: Int) {
        foodList.add(contact)
        foodList = sortingFood(foodList, place)
        adapterList[index] = RecyclerViewAdapter(foodList, place)
    }

    fun getList(place: Int) : MutableList<Contacts> {
        return foodList
    }

    fun removeDB() {
        // TODO
        val deleteFoodIds = mutableListOf<Long>()
        for(i in 0..2) {
            for(foodId in adapterList[i].getDeleteFoodIds()) {
                deleteFoodIds.add(foodId)
            }
        }
        deleteFoodIds.sort()
        if(deleteFoodIds.size > 0) {
            var j = 0
            for (food in foodList) {
                if (food.id == deleteFoodIds[j]) {
                    db?.contactsDao()?.delete(contacts = food) //DB에서 삭제
                    j++
                    if (j == deleteFoodIds.size) break
                }
            }
        }
//        val contacts = foodList[position]
//        db?.contactsDao()?.delete(contacts = contacts) //DB에서 삭제
    }

    fun removeList() {
        val deleteFoodIds = mutableListOf<Long>()
        for(i in 0..2) {
            for(foodId in adapterList[i].getDeleteFoodIds()) {
                deleteFoodIds.add(foodId)
            }
        }
        deleteFoodIds.sort()
        if(deleteFoodIds.size > 0) {
            var j = 0
            for (food in foodList) {
                if (food.id == deleteFoodIds[j]) {
                    foodList.remove(food)
                    j++
                    if (j == deleteFoodIds.size) break
                }
//            foodList.removeAt(position) //리스트에서 삭제
            }
        }
    }

    fun getAdapter(place: Int) : RecyclerViewAdapter {
        return adapterList[place]
    }

    fun getAdapterList() : MutableList<RecyclerViewAdapter> {
        return adapterList
    }

    fun getContactsListSize(place: Int) : Int {
        return foodList.size
    }
}