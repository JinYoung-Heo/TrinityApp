package com.mypackage.naengbiseo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mypackage.naengbiseo.AddData
import com.mypackage.naengbiseo.room.ExcelData
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FoodAddViewModel(
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
) : ViewModel() {
    private val _icon_data = SingleLiveEvent<AddData>() // 내부에서 작동
    val icon_data: LiveData<AddData> get() = _icon_data // 외부로 노출

    fun setIcon(
        foodIcon: Int,
        iconName: String,
        category: String,
        storeWay: String,
        useDate: String,
        treatWay: String
    ) {
        var addData: AddData = AddData(foodIcon, iconName, category, storeWay,useDate, treatWay)
        _icon_data.setValue(addData)
    }

    var TAG = javaClass.simpleName

    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()

    var allExcelData: LiveData<List<ExcelData>> = excelDataRepository.getAllData()

    fun getExcelData(iconName: String): Triple<String, String,String>? {
        var excelPair: Triple<String, String,String>? = Triple("","", "")
        val excelList = allExcelData.value
        if (excelList != null) {
            for (data in excelList) {
                if (data.iconName == iconName) {
                    excelPair = Triple(data.storeWay, data.useDate ,data.treatWay)
                    break
                }
            }
        }
        return excelPair
    }

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }
}