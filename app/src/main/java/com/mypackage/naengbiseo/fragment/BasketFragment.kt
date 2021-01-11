package com.mypackage.naengbiseo.fragment

import android.os.Bundle
import android.util.Log
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
import com.mypackage.naengbiseo.adapter.*
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.*
import kotlinx.android.synthetic.main.basket_food_item.view.*
import kotlinx.android.synthetic.main.fragment_basket.*

class BasketFragment : Fragment() {
    var trashcan_state = 0
    var deletePossible: Int = 0
    val POSSIBLE = 1
    val IMPOSSIBLE = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trashcan_state = 0
        val ac = activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = BasketViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProvider(
            requireParentFragment(),
            factory
        ).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            BasketViewModel::class.java
        )
        var viewAdapter = BasketViewAdapter(viewModel)

        RecyclerViewInBasketFragment.adapter = viewAdapter
        RecyclerViewInBasketFragment.layoutManager = LinearLayoutManager(activity)

        viewModel.updateLiveData.observe(viewLifecycleOwner, Observer {
            Log.d("MSG", "updating data")
            val size = viewModel.basketFoodList.size

            // 모든 아이템 뷰의 EditText의 text으로 foodName 업데이트
            for (position in 0 until size) {
                val itemView = (RecyclerViewInBasketFragment.layoutManager as LinearLayoutManager).findViewByPosition(position)
                if (itemView != null) {
                    viewModel.basketFoodList[position].foodName = itemView.foodNameEditText.text.toString()
                }
            }

            // 지금까지 수정했던 basketFoodList를 가지고 db수정
            for (foodData in viewModel.basketFoodList) {
                viewModel.updateData(foodData)
            }
        })

        // db가 변동될경우 실행됨
        viewModel.allFoodData.observe(viewLifecycleOwner, Observer {
            var foodListToPurchase = it.filter { it.purchaseStatus == 0 }
            foodListToPurchase = foodListToPurchase.sortedBy { foodData -> foodData.foodName }
            viewModel.basketFoodList = foodListToPurchase
            viewAdapter.notifyDataSetChanged()
            val userName = MainActivity.pref_user_name.myEditText
            if (viewModel.basketFoodList.isEmpty()) {
                basketGuideTextView.text = userName + "님, 구매할 식품이 없어요:)"
                plusFoodBtn.visibility = View.INVISIBLE
                basket_trashcan_btn.visibility = View.INVISIBLE
            } else {
                basketGuideTextView.text = userName + "님, 잊지 말고 구매하세요!"
                plusFoodBtn.visibility = View.VISIBLE
                basket_trashcan_btn.visibility = View.VISIBLE
            }
        })

        viewModel.isButtonClickedData.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_basketFragment_to_shoppingCartFragment)
        })

        backButtonInBasketFragment.setOnClickListener {
            findNavController().popBackStack() // 이전 화면으로 이동
        }

        plusFoodBtn.setOnClickListener {
            viewModel.foodAddButtonClicked(true)
        }

        basket_trashcan_btn.setOnClickListener {
            viewModel.setUpdateLiveData(true)
            if (trashcan_state == 0) {
                basket_trashcan_btn.setImageResource(R.drawable.red_trash)
                viewModel.onTrashButton(1)
                trashcan_state = 1
            } else {
                if (viewModel.deleteFoodList.isEmpty()) Log.d("b", "비었어 ")
                viewModel.deleteData()
                trashcan_state = 0
                viewModel.onTrashButton(0)
                viewModel.clearDelData()
                basket_trashcan_btn.setImageResource(R.drawable.basket_trash_btn)
            }
        }

        viewModel.trash_button_event.observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                deletePossible = POSSIBLE
                BasketViewHolder.activateCheckbox()
                (RecyclerViewInBasketFragment.adapter as BasketViewAdapter).notifyDataSetChanged()
            } else {
                deletePossible = IMPOSSIBLE
                BasketViewHolder.inActivateCheckbox()
                (RecyclerViewInBasketFragment.adapter as BasketViewAdapter).notifyDataSetChanged()
            }
        })

        (RecyclerViewInBasketFragment.adapter as BasketViewAdapter).setItemClickListener(object :
            BasketViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                if (deletePossible == POSSIBLE) {
                    v.basket_check_box.toggle()
                    if (v.basket_check_box.isChecked) {
                        viewModel.addDelData(position)
                    } else {
                        viewModel.removeDelData(position)
                    }
                }
            }
        })
    }

    override fun onPause() {
        val ac = activity as MainActivity
        val dao1 = AppDatabase.getInstance(ac).foodDao()
        val dao2 = AppDatabase.getInstance(ac).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = BasketViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProvider(
            requireParentFragment(),
            factory
        ).get( // 메인 액티비티 안쓰고 프래그먼트끼리 뷰모델 공유하는 방법!!!!!! requireParentFragment() 사용하기!!!!
            BasketViewModel::class.java
        )

        // 지금까지 수정했던 값들 업데이트 - basketFragment에서 updateLiveData observe
        viewModel.setUpdateLiveData(true)
        Log.d("MSG", "onPause")
        BasketViewHolder.inActivateCheckbox()
        super.onPause()
    }
}