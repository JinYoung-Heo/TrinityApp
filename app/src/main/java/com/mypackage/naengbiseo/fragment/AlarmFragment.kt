package com.mypackage.naengbiseo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.adapter.AlarmViewAdapter
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.AlarmViewModel
import com.mypackage.naengbiseo.viewmodel.AlarmViewModelFactory
import kotlinx.android.synthetic.main.alarm_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alarm_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac= activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = AlarmViewModelFactory(repository1, repository2)

        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            AlarmViewModel::class.java)
        var viewAdapter = AlarmViewAdapter(viewModel)
        val userName = MainActivity.pref_user_name.myEditText
        val my_d_day = MainActivity.pref_dDay.myEditText.toInt()

        RecyclerViewInAlarmFragment.adapter = viewAdapter
        RecyclerViewInAlarmFragment.layoutManager = LinearLayoutManager(activity)


        // db가 변동될경우 실행됨
        viewModel.allFoodData.observe(viewLifecycleOwner, Observer{
            var dDayFoodList = mutableListOf<Pair<FoodData, Long>>()
            val simpleFormat= SimpleDateFormat("yyyy년 MM월 dd일")
            val today = Calendar.getInstance() // 현재 날짜
            for (foodData in it) { // filtering
                val realExpDate =simpleFormat.parse(foodData.expirationDate) // 문자열로 부터 날짜 들고오기!
                val dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)


                if (dDay + my_d_day > 0 && foodData.buyDate != "1111년 11월 11일" && foodData.purchaseStatus == 1) {

                    dDayFoodList.add(Pair(foodData, dDay))
                }
            }
            var alarmFoodList = dDayFoodList.toList()
            alarmFoodList = alarmFoodList.sortedBy { pair -> pair.second } // d-day를 기준으로 sorting
            viewModel.alarmFoodList = alarmFoodList
            if (viewModel.alarmFoodList.isEmpty()) {
                notification_title.text = userName + "님, 식품들이 신선하네요!"
            } else {
                notification_title.text = userName + "님, 얼른 드셔야해요!"
            }
            viewAdapter.notifyDataSetChanged()
        })

        settingButton.setOnClickListener {
            findNavController().navigate(R.id.alarmSettingFragment)
        }

        backButtonInAlarmFragment.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onStop() { // back버튼, 홈버튼 누를 시에도 장바구니 최신 데이터를 db에 저장해야하기때문
        super.onStop()
    }
}