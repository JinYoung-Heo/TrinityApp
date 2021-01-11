package com.mypackage.naengbiseo.room

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExcelDataRepository private constructor(private val excelDao: ExcelDao) {
    fun getAllData() = excelDao.getAll()


    suspend fun insert(excelData: ExcelData) {
        withContext(Dispatchers.IO) {
            excelDao.insertData(excelData)
        }
    }

    suspend fun delete(excelData: ExcelData) {
        withContext(Dispatchers.IO) {
            excelDao.deleteData(excelData)
        }
    }

    suspend fun update(excelData: ExcelData) {
        withContext(Dispatchers.IO) {
            excelDao.updateData(excelData)
        }
    }

    companion object {
        @Volatile
        private var instance: ExcelDataRepository? = null

        fun getInstance(excelDao: ExcelDao) =
            instance ?: synchronized(this) {
                instance
                    ?: ExcelDataRepository(excelDao)
                        .also { instance = it }
            }

    }
}