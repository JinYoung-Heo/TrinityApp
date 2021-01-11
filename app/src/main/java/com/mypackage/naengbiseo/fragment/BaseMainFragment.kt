package com.mypackage.naengbiseo.fragment

import android.app.AlertDialog
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
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.adapter.FoodViewHolder
import com.mypackage.naengbiseo.adapter.ViewPagerAdapter2
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.MainViewModel
import com.mypackage.naengbiseo.viewmodel.MainViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_base_main.*
import kotlinx.android.synthetic.main.host_activity.*

class BaseMainFragment :Fragment(){
    private val tabTextList = arrayListOf("선반", "냉장", "냉동")
    var trashcan_state = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_base_main, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

        trashcan_state = 0

        // 아 이게 각 프래그먼트마다 notify를 통해 화면 갱신을 해주니 여기서 일일이 옵저브 할 필요가 없네!!
        // 왜냐면 여기도 뷰모델 온트래쉬버튼(트래시 버튼 이벤트를 call 함)을 이용해 알려주기 때문!
        trashcan_btn.setOnClickListener {
            if(trashcan_state == 0) {
                trashcan_btn.setImageResource(R.drawable.red_trash)
                viewModel.onTrashButton(1)
                trashcan_state = 1
            }
            else {
                if(viewModel.getDelData()==null) Log.d("b","비었어 ")
                viewModel.deleteData()
                trashcan_state = 0
                viewModel.onTrashButton(0)
                viewModel.clearDelData()
                trashcan_btn.setImageResource(R.drawable.trash)
            }
        }

        view_pager_main.adapter =
            ViewPagerAdapter2(
                childFragmentManager,
                lifecycle
            )
        view_pager_main.offscreenPageLimit = 2 //프래그먼트 깨지는거 방지
        TabLayoutMediator(mainActivity.tabLayout, view_pager_main) { //탭레이아웃과 뷰페이저 연결
                tab, position ->
            //view_pager_main.currentItem = args.a1.toString().toInt()
            tab.text = tabTextList[position]
        }.attach()
        //view_pager_main.currentItem = args.ar.toInt();
        view_pager_main.setCurrentItem(1, false)

        viewModel.location_data.observe(viewLifecycleOwner, Observer{
            view_pager_main.setCurrentItem(it, false)
        })

        go_to_add_item_page_btn.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_foodAddFragment)
        }

        viewModel.deleteDataList.observe(viewLifecycleOwner, Observer{
            val foodListToBasket = it
            if (foodListToBasket.isNotEmpty()) {
                val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null)
                dialogView.dialogTextView1.text = MainActivity.pref_user_name.myEditText + "님, 다 드셨네요!"
                //AlertDialogBuilder
                val builder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setCancelable(false)
                //show dialog
                val alertDialog = builder.show()
                dialogView.dialogYesButton.setOnClickListener {
                    //dismiss dialog
                    alertDialog.dismiss()
                    for (foodData in foodListToBasket) {
                        foodData.purchaseStatus = 0
                        viewModel.insertData(foodData)
                    }
                }
                //cancel button click of custom layout
                dialogView.dialogNoButton.setOnClickListener {
                    //dismiss dialog
                    alertDialog.dismiss()
                }
            }
        })
    }

    override fun onPause() {
        FoodViewHolder.inActivateCheckbox()
        super.onPause()
    }
}