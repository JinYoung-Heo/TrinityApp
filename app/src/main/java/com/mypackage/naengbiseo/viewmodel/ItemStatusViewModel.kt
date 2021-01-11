package com.mypackage.naengbiseo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ItemStatusViewModel(private val foodDataRepository: FoodDataRepository) : ViewModel() {
    private val _compare_data = SingleLiveEvent<Triple<String, String, String>>() // 내부에서 작동
    val compare_data: LiveData<Triple<String, String, String>> get() = _compare_data // 외부로 노출

    fun setCompareData(foodName: String, storeLocation: String, buyDate: String) {
        var compareTriple: Triple<String, String, String> = Triple(foodName, storeLocation, buyDate)
        _compare_data.setValue(compareTriple)
    }


    fun getFoodData(): FoodData? {
        var foodList = allFoodData.value
        var compareData = compare_data.value
        var returnFoodData: FoodData? = null
        for (foodData in foodList!!) { // !!로 null check
            if (compareUniqueEntity(foodData, compareData!!)) {
                returnFoodData = foodData
                return returnFoodData
            }
        }
        return returnFoodData
    }

    fun compareUniqueEntity(
        foodData: FoodData,
        compareTriple: Triple<String, String, String>
    ): Boolean {
        return foodData.foodName == compareTriple.first && foodData.storeLocation == compareTriple.second && foodData.buyDate == compareTriple.third
    }

    var TAG = javaClass.simpleName

    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()


    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }
}