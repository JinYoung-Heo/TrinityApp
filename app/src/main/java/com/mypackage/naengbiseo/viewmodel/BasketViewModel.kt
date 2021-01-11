package com.mypackage.naengbiseo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mypackage.naengbiseo.FoodIcon
import com.mypackage.naengbiseo.room.ExcelData
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BasketViewModel (
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
): ViewModel() {
    private val _isButtonClickedData = SingleLiveEvent<Boolean>() // 내부에서 작동
    val isButtonClickedData: LiveData<Boolean> get() = _isButtonClickedData // 외부로 노출
    private val _updateLiveData = SingleLiveEvent<Boolean>() // 내부에서 작동
    val updateLiveData: LiveData<Boolean> get() = _updateLiveData // 외부로 노출
    private val _trash_button_event = SingleLiveEvent<Int>()
    val trash_button_event: LiveData<Int> get() = _trash_button_event

    var basketFoodList = listOf<FoodData>()
    var deleteFoodList = mutableListOf<FoodData>()

    fun onTrashButton(i: Int) {
        _trash_button_event.value = i
    }

    fun addDelData(position: Int) {
        Log.d("dd", "추가됐음")
        deleteFoodList.add(basketFoodList[position])
    }

    fun removeDelData(position: Int) {
        Log.d("dd", "제거됐음")
        deleteFoodList.remove(basketFoodList[position])
    }

    fun deleteData() {
        for (foodData in deleteFoodList) {
            viewModelScope.launch {
                foodDataRepository.delete(foodData)
            }
        }
    }

    fun clearDelData() {
        deleteFoodList.clear()
    }

    fun setUpdateLiveData(state: Boolean) {
        _updateLiveData.value = state
    }

    fun getBasketFoodAt(position: Int): FoodData {
        return basketFoodList[position]
    }

    fun getFoodToPurchase(): List<FoodData> {
        val allData = allFoodData.value
        val foodListToPurchase = mutableListOf<FoodData>()
        if (allData != null) {
            for (foodData in allData) {
                if (foodData.purchaseStatus == 0) {
                    foodListToPurchase.add(foodData)
                }
            }
        }
        return foodListToPurchase.toList()
    }

    fun delData(position: Int) {
        viewModelScope.launch { foodDataRepository.delete(basketFoodList[position]) }
    }

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }

    fun updateData(foodData: FoodData) {
        viewModelScope.launch {
            foodDataRepository.update(foodData)
        }
    }

    fun foodAddButtonClicked(isButtonClicked: Boolean) {
        _isButtonClickedData.setValue(isButtonClicked)
    }

    var TAG = javaClass.simpleName

    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()

    var allExcelData: LiveData<List<ExcelData>> = excelDataRepository.getAllData()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getBasketData(): List<FoodData> {
        var basketList = mutableListOf<FoodData>()
        val foodList = allFoodData.value
        if(foodList != null){
            for(foodData in foodList){
                if(foodData.purchaseStatus == 0){
                    basketList.add(foodData)
                }
            }
        }
        return basketList.toList()
    }

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

    fun insertDataList(foodDataList: MutableList<FoodIcon>) {
        viewModelScope.launch {
            for (foodData in foodDataList) {
                val excelPair = getExcelData(foodData.iconName)
                Log.d("1st","메롱")
                Log.d("2nd",excelPair!!.second)
                val storeWay = excelPair!!.first
                val useDate = excelPair!!.second
                val treatWay = excelPair!!.third
                foodDataRepository.insert(FoodData(useDate = useDate,foodName = foodData.iconName, storeLocation = "cool", foodIcon = foodData.iconResource, purchaseStatus = 0, foodCategory = foodData.category, foodNumber = 1, storeWay = storeWay, treatWay = treatWay, expirationDate = "1111년 11월 11일", buyDate = "1111년 11월 11일", uniqueId = (Int.MIN_VALUE..Int.MAX_VALUE).random()))
            }
        }
    }
}