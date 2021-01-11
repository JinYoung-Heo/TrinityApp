package com.mypackage.naengbiseo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class AlarmViewModel(
    private val foodDataRepository: FoodDataRepository,
    private val excelDataRepository: ExcelDataRepository
): ViewModel() {
    private val _alarmHour = SingleLiveEvent<String>() // 내부에서 작동
    val alarmHour: LiveData<String> get() = _alarmHour // 외부로 노출
    private val _alarmMinute = SingleLiveEvent<String>() // 내부에서 작동
    val alarmMinute: LiveData<String> get() = _alarmMinute // 외부로 노출
    private val _alarm_am_or_pm = SingleLiveEvent<String>() // 내부에서 작동
    val alarm_am_or_pm: LiveData<String> get() = _alarm_am_or_pm // 외부로 노출
    private val _userName = SingleLiveEvent<String>() // 내부에서 작동
    val userName: LiveData<String> get() = _userName // 외부로 노출
    private val _dDay = SingleLiveEvent<Int>() // 내부에서 작동
    val dDay: LiveData<Int> get() = _dDay // 외부로 노출
    private val _alarmState = SingleLiveEvent<String>() // 내부에서 작동
    val alarmState: LiveData<String> get() = _alarmState // 외부로 노출

    var TAG = javaClass.simpleName

    /** 뷰모델에서 모델로 데이터를 넣기위한거?*/
    var allFoodData: LiveData<List<FoodData>> = foodDataRepository.getAllData()

    var alarmFoodList = listOf<Pair<FoodData, Long>>()

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getAlarmFoodAt(position: Int): Pair<FoodData, Long> {
        return alarmFoodList[position]
    }

    fun setAlarmState(isChecked: String) {
        MainActivity.pref_alarm_state.myEditText = isChecked
        _alarmState.value = isChecked
    }

    fun set_d_day(dDay: Int) {
        MainActivity.pref_dDay.myEditText = dDay.toString()
        _dDay.value = dDay
    }

    fun setUserName(name: String) {
        var myName = name
        if (myName.isBlank()) {
            myName = "이름없음"
        }
        MainActivity.pref_user_name.myEditText = myName
        _userName.value = myName
    }

    fun setHour(hour: String) {
        MainActivity.pref_hour.myEditText = hour

        var intHour = hour.toInt()
        var strHour = hour
        val am_or_pm: String

        if (intHour < 12) {
            am_or_pm = "오전"
        } else {
            am_or_pm = "오후"
        }
        MainActivity.pref_am_or_pm.myEditText = am_or_pm
        _alarm_am_or_pm.value = am_or_pm

        if (intHour > 12) {
            intHour -= 12
            strHour = intHour.toString()
        }
        if (intHour < 10) {
            strHour = "0" + strHour
        }
        _alarmHour.value = strHour
    }

    fun setMinute(minute: String) {
        MainActivity.pref_minute.myEditText = minute

        var strMinute = minute

        if (strMinute.toInt() < 10) {
            strMinute = "0" + strMinute
        }
        _alarmMinute.value = strMinute
    }
}