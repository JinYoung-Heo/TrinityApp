package com.mypackage.naengbiseo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodDataRepository

class AlarmViewModelFactory(
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(foodDataRepository, excelDataRepository) as T
    }
}