package com.mypackage.naengbiseo.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodDataRepository private constructor(private val foodDao: FoodDao) {
    fun getAllData() = foodDao.getAll()

    fun getAllData2() = foodDao.getAll2()

    suspend fun insert(foodData: FoodData) {
        withContext(Dispatchers.IO) {
            foodDao.insertData(foodData)
        }
    }

    suspend fun delete(foodData: FoodData) {
        withContext(Dispatchers.IO) {
            foodDao.deleteData(foodData)
        }
    }

    suspend fun update(foodData: FoodData) {
        withContext(Dispatchers.IO) {
            foodDao.updateData(foodData)
        }
    }

    companion object {
        @Volatile
        private var instance: FoodDataRepository? = null

        fun getInstance(userDao: FoodDao) =
            instance ?: synchronized(this) {
                instance
                    ?: FoodDataRepository(userDao)
                        .also { instance = it }
            }

    }
}