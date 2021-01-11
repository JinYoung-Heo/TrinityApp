package com.mypackage.naengbiseo.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExcelDao {
    @Query("SELECT * FROM excelData")
    fun getAll(): LiveData<List<ExcelData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(inputUserData: ExcelData)

    @Delete // db 데이터 삭제기능
    fun deleteData(food: ExcelData)

    @Update
    fun updateData(food: ExcelData)

}