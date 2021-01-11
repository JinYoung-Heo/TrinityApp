package com.mypackage.naengbiseo.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.adapter.FoodViewAdapter
import com.mypackage.naengbiseo.adapter.FoodViewHolder
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.*
import kotlinx.android.synthetic.main.food_item.view.*
import kotlinx.android.synthetic.main.food_item.view.food_name
import kotlinx.android.synthetic.main.fragment_cold.*
import java.text.SimpleDateFormat
import java.util.*

class ColdFragment : Fragment() {
    var simpleFormat: SimpleDateFormat = SimpleDateFormat("yyyy년 M월 d일")
    var simpleFormat2:SimpleDateFormat = SimpleDateFormat("yyyy. MM. dd")
    var realBuyDate: Date? = null
    lateinit var dateString:String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    var sel: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cold, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao1 = AppDatabase.getInstance(mainActivity).foodDao()
        val dao2 = AppDatabase.getInstance(mainActivity).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = MainViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java)
//        viewModel.del_data.observe(viewLifecycleOwner, Observer{})
        /*val statusFactory = ItemStatusViewModelFactory(repository)
        var statusViewModel = ViewModelProvider(
            requireParentFragment(),
            statusFactory
        ).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            ItemStatusViewModel::class.java
        )*/
        //viewModel.initSortData()
        viewModel.sort_cold_data.observe(viewLifecycleOwner, Observer {
            Log.d("a","반응 왔다")
            viewModel.sort()
            (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged()
        })

        viewModel.allFoodData.observe(viewLifecycleOwner, Observer {
            viewModel.sort()
            (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged() // 이게 중요
        }) // 버튼안에 옵저브를 안넣더라도 항상 옵저브하고 있어야 room 의 userdata 를 쓸수 있다,



        //adapter 추가

        search_recyclerview_cold.adapter =
            FoodViewAdapter(viewModel, 2)
        //레이아웃 매니저 추가
        search_recyclerview_cold.layoutManager = LinearLayoutManager(activity)

        (search_recyclerview_cold.adapter as FoodViewAdapter).setItemClickListener(object :
            FoodViewAdapter.OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onClick(v: View, position: Int) {

                if(v.id == R.id.food_item_layout){
                    if(v.buy_date.text.toString() == "재료 정보를 기입해주세요"){
                        realBuyDate = simpleFormat2.parse("1111. 11. 11")
                        dateString = simpleFormat.format(realBuyDate)
                    }
                    else{
                        realBuyDate = simpleFormat2.parse(v.buy_date.text.toString())
                        dateString = simpleFormat.format(realBuyDate)
                    }
                    if (sel == 1) {
                        v.check_box.toggle()
                        if (v.check_box.isChecked) {
                            viewModel.addDelData(v.food_name.text.toString(),"cold",dateString,v.unique_id.text.toString().toInt())
                        } else {
                            viewModel.removeDelData(v.food_name.text.toString(),"cold",dateString,v.unique_id.text.toString().toInt())
                        }
                    }
                    else {
                        if(v.buy_date.text.toString() == "재료 정보를 기입해주세요"){
                            viewModel.setCompareData(
                                v.food_name.text.toString(),
                                "cold",
                                "1111. 11. 11",
                                v.unique_id.text.toString().toInt()
                            )
                        }
                        else{
                            viewModel.setCompareData(
                                v.food_name.text.toString(),
                                "cold",
                                v.buy_date.text.toString(),
                                v.unique_id.text.toString().toInt()
                            )
                        }
                        findNavController().navigate(R.id.itemStatusFragment)
                    }
                }
                else {

                }
            }
        })

        viewModel.trash_button_cold_event.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                sel = 1
                FoodViewHolder.activateCheckbox()
                (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged()
            } else {
                sel = 0
                FoodViewHolder.inActivateCheckbox()
                (search_recyclerview_cold.adapter as FoodViewAdapter).notifyDataSetChanged()
            }

        })
    }
}