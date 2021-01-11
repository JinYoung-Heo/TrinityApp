package com.mypackage.naengbiseo.fragment

import androidx.lifecycle.Observer
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodData
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.FoodAddViewModel
import com.mypackage.naengbiseo.viewmodel.FoodAddViewModelFactory
import com.mypackage.naengbiseo.viewmodel.MainViewModel
import com.mypackage.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_food_add.*
import java.lang.NumberFormatException
import java.util.*

class FoodAddFragment: Fragment() {
    private val TYPE_NO_ICON = -1
    var foodIcon: Int = 0
    var foodCategory:String = ""
    var storeWay:String = ""
    var useDate:String = ""
    var treatWay:String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_add, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac=activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = FoodAddViewModelFactory(repository1,repository2)
        var viewModel = ViewModelProvider(requireParentFragment(), factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            FoodAddViewModel::class.java)
        val factory2 = MainViewModelFactory(repository1,repository2)
        var viewModel2 = ViewModelProvider(activity as MainActivity, factory).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            MainViewModel::class.java)

        back_button.setOnClickListener {
            findNavController().navigateUp()
        }
        go_to_select_button.setOnClickListener {
            findNavController().navigate(R.id.action_foodAddFragment_to_foodIconAddFragment)
        }

        viewModel.icon_data.observe(viewLifecycleOwner, Observer{
            Log.d("sd","변경됐냐?!!")
            foodIcon = it.getfoodIcon
            foodCategory = it.getCategory
            storeWay = it.getStoreWay
            useDate = it.getUseDate
            treatWay = it.getTreatWay
            go_to_select_button.setImageResource(it.getfoodIcon)
            food_edit_text.setText(it.getIconName)
        })

        add_food_btn.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            val food_number_text = food_number_edit_text.text.toString()
            val expiration_date = expiration_date_text.text.toString()
            val purchase_date=purchase_date_text.text.toString()
            val memo=memo_edit_text.text.toString()

            val radio_btn_id = radio_group.checkedRadioButtonId

            if(food_name == null || food_name.isEmpty() || food_number_text == null || food_number_text.isEmpty()
                || expiration_date == null || expiration_date.isEmpty() || purchase_date == null || purchase_date.isEmpty()
                    ) {
                Toast.makeText(activity as MainActivity,"필수정보를 입력해주세요!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try{
                val food_number = food_number_text.toInt()
                when(radio_btn_id) {
                    radio_btn1.id -> {
                        viewModel.insertData(FoodData(useDate = useDate,storeWay = storeWay,treatWay = treatWay,foodCategory = foodCategory,foodName=food_name, storeLocation = "shelf",foodNumber = food_number,buyDate = purchase_date,expirationDate = expiration_date,foodMemo = memo,foodIcon=foodIcon, uniqueId = (Int.MIN_VALUE..Int.MAX_VALUE).random()) ) // 음식 정보 저장
                        food_edit_text.setText("")
                        viewModel2.setLocation(0)
                    }
                    radio_btn2.id -> {
                        viewModel.insertData(FoodData(useDate = useDate,storeWay = storeWay,treatWay = treatWay,foodCategory = foodCategory,foodName=food_name, storeLocation = "cool",foodNumber = food_number,buyDate = purchase_date,expirationDate = expiration_date,foodMemo = memo,foodIcon=foodIcon, uniqueId = (Int.MIN_VALUE..Int.MAX_VALUE).random())) //Contacts 생성
                        food_edit_text.setText("")
                        viewModel2.setLocation(1)
                    }
                    radio_btn3.id -> {
                        viewModel.insertData(FoodData(useDate = useDate,storeWay = storeWay,treatWay = treatWay,foodCategory = foodCategory,foodName=food_name, storeLocation = "cold",foodNumber = food_number,buyDate = purchase_date,expirationDate = expiration_date,foodMemo = memo,foodIcon=foodIcon, uniqueId = (Int.MIN_VALUE..Int.MAX_VALUE).random())) //Contacts 생성
                        food_edit_text.setText("")
                        viewModel2.setLocation(2)
                    }
                    else -> {
                        Toast.makeText(activity as MainActivity,"음식을 보관할 장소를 선택해주세요!!", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
            } catch(e:NumberFormatException){
                Toast.makeText(activity as MainActivity,"수량에는 숫자를 입력해주세요!",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            findNavController().navigate(R.id.action_foodAddFragment_to_mainFragment)
        }

        purchase_button.setOnClickListener{
            val cal1 = Calendar.getInstance()
            DatePickerDialog(activity as MainActivity,R.style.DatePickerTheme, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                var M= m+1
                purchase_date_text.text="$y"+"년 "+"$M"+"월 "+"$d"+"일" }, // 이상하게 월은 0월부터네.. +1 해주자
                cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE)).show()
        }

        expiration_button.setOnClickListener {
            val cal2 = Calendar.getInstance()
            DatePickerDialog(activity as MainActivity,R.style.DatePickerTheme, DatePickerDialog.OnDateSetListener { datePicker, y, m, d->
                var M= m+1
                expiration_date_text.text="$y"+"년 "+"$M"+"월 "+"$d"+"일" },
                cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH), cal2.get(Calendar.DATE)).show()
        }

    }
}