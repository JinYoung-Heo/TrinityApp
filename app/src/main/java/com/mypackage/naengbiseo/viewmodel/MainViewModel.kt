package com.mypackage.naengbiseo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mypackage.naengbiseo.DelData
import com.mypackage.naengbiseo.room.ExcelData
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
) : ViewModel() {


    var allExcelData: LiveData<List<ExcelData>> = excelDataRepository.getAllData()

    fun excelIsEmpty(): Boolean {
        return allExcelData.value == null
    }

    private val _del_data = SingleLiveEvent<MutableList<DelData>>()
    val del_data: LiveData<MutableList<DelData>> get() = _del_data
    val copyDelList: MutableList<DelData> =
        mutableListOf<DelData>()

    fun addDelData(foodName: String, storeLocation: String, buyDate: String, uniqueId: Int) {
        Log.d("dd", "추가됐음")
        copyDelList.add(DelData(foodName, storeLocation, buyDate, uniqueId))
        _del_data.setValue(copyDelList)
    }

    fun removeDelData(foodName: String, storeLocation: String, buyDate: String, uniqueId: Int) {
        Log.d("dd", "제거됐음")
        copyDelList.remove(DelData(foodName, storeLocation, buyDate, uniqueId))
        _del_data.setValue(copyDelList)
    }

    fun clearDelData() {
        _del_data.value?.clear()
    }

    fun getDelData(): MutableList<DelData>? {
        return _del_data.value
    }

    var TAG = javaClass.simpleName


    private val _trash_button_cool_event = SingleLiveEvent<Int>()
    val trash_button_cool_event: LiveData<Int> get() = _trash_button_cool_event

    private val _trash_button_cold_event = SingleLiveEvent<Int>()
    val trash_button_cold_event: LiveData<Int> get() = _trash_button_cold_event

    private val _trash_button_shelf_event = SingleLiveEvent<Int>()
    val trash_button_shelf_event: LiveData<Int> get() = _trash_button_shelf_event

    private val _searchText = SingleLiveEvent<String>()
    val searchText get() = _searchText

    private val _deleteDataList = SingleLiveEvent<MutableList<FoodData>>()
    val deleteDataList get() = _deleteDataList

    fun onTrashButton(i: Int) {
        /** 뷰의 onClickListener 호출될때 얘를 호출해 싱글라이브데이터 의 Setdata를 call
        뷰와 뷰모델의 연결?....*/
        _trash_button_cool_event.setValue(i)
        _trash_button_cold_event.setValue(i)
        _trash_button_shelf_event.setValue(i)
    }


    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()


    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun insertData(foodData: FoodData) {
        viewModelScope.launch { foodDataRepository.insert(foodData) }
    }

    fun updateData(foodData: FoodData) {
        viewModelScope.launch {
            foodDataRepository.update(foodData)
        }
    }

    fun insertExcelData(excelData: ExcelData) {
        viewModelScope.launch { excelDataRepository.insert(excelData) }
    }

    fun deleteData() {
        var foodList: List<FoodData>? = allFoodData.value
        var dList = del_data.value
        val delDataList = mutableListOf<FoodData>()
        if (foodList != null) {
            if (dList != null) {
                for (triple in dList) {
                    for (foodData in foodList) {
                        if (foodData.foodName == triple.getFoodName && foodData.storeLocation == triple.getStoreLocation && foodData.buyDate == triple.getBuyDate && foodData.uniqueId == triple.getUniqueId) {
                            delDataList.add(foodData)
                            viewModelScope.launch {
                                foodDataRepository.delete(foodData)
                            }
                        }
                    }
                }
                _deleteDataList.value = delDataList
            }
        }

    }


    var fl: MutableList<FoodData>? = null
    private val _sort_data = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val sort_data: LiveData<Int> get() = _sort_data

    private val _sort_cold_data = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val sort_cold_data: LiveData<Int> get() = _sort_cold_data

    private val _sort_shelf_data = SingleLiveEvent<Int>() // 정렬 관련 라이브 데이터
    val sort_shelf_data: LiveData<Int> get() = _sort_shelf_data

    fun initSortData() {
        _sort_data.setValue(0)
    }

    fun setSortData(i: Int) {
        Log.d("s", i.toString())
        _sort_data.setValue(i)
        _sort_cold_data.setValue(i)
        _sort_shelf_data.setValue(i)
    }


    fun sortedByCategory(): MutableList<FoodData> {
        var sortedList = mutableListOf<FoodData>()
        var foodList: List<FoodData>? = allFoodData.value
        var categoryList = getCategory()
        if (categoryList != null) {
            for (category in categoryList!!) {
                sortedList.add(
                    FoodData(
                        foodCategory = category.first,
                        header = 1,
                        storeLocation = category.second
                    )
                )
                //Log.d("sd",category)
                for (data in foodList!!) {
                    if (data.foodCategory == category.first && data.storeLocation == category.second) sortedList.add(
                        data
                    )
                }
            }
        }

        return sortedList
    }

    fun sortedByBuyDate(): MutableList<FoodData> {
        var sortedList = mutableListOf<FoodData>()
        var foodList: List<FoodData>? = allFoodData.value
        var buyDateList = getBuyDate()
        if (buyDateList != null) {
            for (date in buyDateList!!) {
                sortedList.add(
                    FoodData(
                        buyDate = date.first,
                        header = 1,
                        storeLocation = date.second
                    )
                )
                for (data in foodList!!) {
                    if (data.buyDate == date.first && data.storeLocation == date.second) sortedList.add(
                        data
                    )
                }
            }
        }

        return sortedList
    }

    fun sort() {
        when (sort_data.value) {
            0 -> {
                fl = sortedByCategory()
            }
            1 -> {
                fl = sortedByBuyDate()
            }
            2 -> {
                fl =
                    allFoodData.value!!.sortedBy { getTime(it.expirationDate) }
                        .toMutableList()
                //fl!!.add(0,FoodData(header = 1))
            }
            else -> {
                fl = allFoodData.value!!.toMutableList()
            }
        }
    }

    fun getCategory(): MutableList<Pair<String, String>> {
        var foodList: List<FoodData>? = allFoodData.value
        var categorySet: MutableSet<Pair<String, String>> = mutableSetOf()
        var categoryList: MutableList<Pair<String, String>> = mutableListOf()
        if (foodList != null) {
            for (data: FoodData in foodList!!) {
                if (data.purchaseStatus != 0) {
                    categorySet.add(Pair(data.foodCategory, data.storeLocation))
                }
            }
            categoryList = categorySet.toList()!!.sortedBy { it.first }.toMutableList()
        }

        return categoryList
    }

    fun getBuyDate(): MutableList<Pair<String, String>> {
        var foodList: List<FoodData>? = allFoodData.value
        var buyDateSet: MutableSet<Pair<String, String>> = mutableSetOf()
        var buyDateList: MutableList<Pair<String, String>> = mutableListOf()
        if (foodList != null) {
            for (data: FoodData in foodList!!) {
                if (data.purchaseStatus != 0) {
                    buyDateSet.add(Pair(data.buyDate, data.storeLocation))
                }
            }
            buyDateList = buyDateSet.toList()!!.sortedBy { getTime(it.first) }.toMutableList()
        }

        return buyDateList
    }


    fun getTime(date: String): Long {
        var simpleFormat = SimpleDateFormat("yyyy년 MM월 dd일")
        var realExpDate = simpleFormat.parse(date) // 문자열로 부터 날짜 들고오기!
        return realExpDate.time
    }

    fun getColdData(): List<FoodData> {
        var coldFoodData: MutableList<FoodData> = mutableListOf()

        if (fl != null) {
            for (data: FoodData in fl!!) {
                if (data.storeLocation == "cold" && data.purchaseStatus == 1) {
                    coldFoodData.add(data)
                }
            }
/*
            for(cData in coldFoodData){
                sortDataSet.add(cData.)
            }*/
        }
        if (coldFoodData.isEmpty()) {
            coldFoodData.add(FoodData(Null = 1))
        }
        return coldFoodData.toList()
    }

    fun getCoolData(): List<FoodData> {
        var coolFoodData: MutableList<FoodData> = mutableListOf()
        if (fl != null) {
            for (data: FoodData in fl!!) {
                if (data.storeLocation == "cool" && data.purchaseStatus == 1) {
                    coolFoodData.add(data)
                }
            }
        }
        if (coolFoodData.isEmpty()) {
            coolFoodData.add(FoodData(Null = 1))
        }
        return coolFoodData.toList()
    }

    fun getShelfData(): List<FoodData> {
        var shelfFoodData: MutableList<FoodData> = mutableListOf()
        if (fl != null) {
            for (data: FoodData in fl!!) {
                if (data.storeLocation == "shelf" && data.purchaseStatus == 1) {
                    shelfFoodData.add(data)
                }
            }
        }
        if (shelfFoodData.isEmpty()) {
            shelfFoodData.add(FoodData(Null = 1))
        }
        return shelfFoodData.toList()
    }


    // 재료 정보창으로 정보 넘기기 위한것. 시작
    private val _compare_data = SingleLiveEvent<DelData>() // 내부에서 작동
    val compare_data: LiveData<DelData> get() = _compare_data // 외부로 노출

    fun setCompareData(foodName: String, storeLocation: String, buyDate: String, uniqueId: Int) {
        var compareTriple: DelData = DelData(foodName, storeLocation, buyDate, uniqueId)
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
        compareTriple: DelData
    ): Boolean {
        var simpleFormat = SimpleDateFormat("yyyy년 M월 d일")
        var simpleFormat2 = SimpleDateFormat("yyyy. MM. dd")

        var realExpDate = simpleFormat2.parse(compareTriple.getBuyDate) // 문자열로 부터 날짜 들고오기!

        var dateString = simpleFormat.format(realExpDate)
        Log.d("buy", foodData.buyDate)
        Log.d("store", foodData.storeLocation)
        Log.d("name", foodData.foodName)
        Log.d("ss", foodData.uniqueId.toString())
        Log.d("buy", dateString)
        Log.d("store", compareTriple.getStoreLocation)
        Log.d("name", compareTriple.getFoodName)
        Log.d("ss", compareTriple.getUniqueId.toString())
        Log.d("ss"," ")
        return foodData.foodName == compareTriple.getFoodName && foodData.storeLocation == compareTriple.getStoreLocation && foodData.buyDate == dateString && foodData.uniqueId == compareTriple.getUniqueId
    }
    // 재료 정보창으로 정보 넘기기 위한것. 끝


    // 검색창 프래그먼트 관련
    private val _search_data = SingleLiveEvent<String>() // 내부에서 작동
    val search_data: LiveData<String> get() = _search_data // 외부로 노출

    fun setSearchData(text: String) {
        _search_data.setValue(text)
    }

    fun getSearchData(): List<FoodData>? {
        var foodList = allFoodData.value
        var text = _search_data.value
        var searchDataList: MutableList<FoodData> = mutableListOf()
        /*if (foodList != null) {
            searchDataList =
                foodList.filter { it.foodName.contains(_search_data.value!!) }.toMutableList()
            searchDataList.add(0, FoodData(header = 1))
        } */
        if (text == null || text == "") {
            searchDataList.add(FoodData(header = 1))

        } else if (foodList != null && text != null) {
            for (data in foodList) {
                if (data.foodName.contains(text)) {
                    searchDataList.add(data)
                }
            }
            if (searchDataList.size == 0) {
                searchDataList.add(FoodData(Null = 1))
            } else searchDataList.add(0, FoodData(header = 1))
        }

        return searchDataList.toList()
    }

    private val _location_data = SingleLiveEvent<Int>() // 내부에서 작동
    val location_data: LiveData<Int> get() = _location_data // 외부로 노출

    fun setLocation(i: Int) {
        _location_data.setValue(i)
    }

    fun basketIn(): Boolean {
        var flag: Boolean = false
        val foodList = allFoodData.value
        if (foodList != null) {
            for (foodData in foodList) {
                if (foodData.purchaseStatus == 0) {
                    flag = true
                }
            }
        }
        return flag
    }

    fun alarmIn(): Boolean {
        var flag: Boolean = false
        var simpleFormat: SimpleDateFormat
        var realExpDate: Date?
        var today: Calendar?
        var dDay: Long
        val foodList = allFoodData.value
        if (foodList != null) {
            for (foodData in foodList) {
                simpleFormat = SimpleDateFormat("yyyy년 MM월 dd일")
                realExpDate = simpleFormat.parse(foodData.expirationDate) // 문자열로 부터 날짜 들고오기!
                today = Calendar.getInstance() // 현재 날짜
                dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)
                if (dDay >= 0 || (Math.abs(dDay - 1) < 4 && dDay < 0)) {
                    if (foodData.expirationDate != "1111년 11월 11일" && foodData.purchaseStatus != 0) {
                        flag = true
                        break
                    }
                }
            }
        }
        return flag
    }
}