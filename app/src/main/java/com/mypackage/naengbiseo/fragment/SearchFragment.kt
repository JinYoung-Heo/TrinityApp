package com.mypackage.naengbiseo.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.adapter.SearchViewAdapter
import com.mypackage.naengbiseo.room.AppDatabase
import com.mypackage.naengbiseo.room.ExcelDataRepository
import com.mypackage.naengbiseo.room.FoodDataRepository
import com.mypackage.naengbiseo.viewmodel.MainViewModel
import com.mypackage.naengbiseo.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.food_item_search_version.view.*
import kotlinx.android.synthetic.main.food_item_search_version.view.buy_date
import kotlinx.android.synthetic.main.food_item_search_version.view.food_name
import kotlinx.android.synthetic.main.food_item_search_version.view.unique_id
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    private lateinit var callback: OnBackPressedCallback
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프래그먼트에서 키보드 올리기
        val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        search_food_edit_text.requestFocus()
        imm?.showSoftInput(search_food_edit_text,0)

        val mainActivity = activity as MainActivity // 프래그먼트에서 액티비티 접근하는 법 꼭 기억하자!!!!
        val dao1 = AppDatabase.getInstance(mainActivity).foodDao()
        val dao2 = AppDatabase.getInstance(mainActivity).excelDao()
        val repository1 = FoodDataRepository.getInstance(dao1)
        val repository2 = ExcelDataRepository.getInstance(dao2)
        val factory = MainViewModelFactory(repository1, repository2)
        var viewModel = ViewModelProviders.of(activity as MainActivity, factory).get(
            MainViewModel::class.java
        )


        search_recyclerview.adapter = SearchViewAdapter(viewModel)
        search_recyclerview.layoutManager = LinearLayoutManager(activity)

        viewModel.search_data.observe(viewLifecycleOwner, Observer {
            (search_recyclerview.adapter as SearchViewAdapter).notifyDataSetChanged()
        })

        (search_recyclerview.adapter as SearchViewAdapter).setItemClickListener(object :
            SearchViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {

                var location_text: String = ""
                Log.d("s", "클릭")
                when (v.slt.text.toString()) {
                    "선반" -> {
                        location_text = "shelf"
                    }
                    "냉장" -> {
                        location_text = "cool"
                    }
                    "냉동" -> {
                        location_text = "cold"
                    }
                }
                if(v.buy_date.text.toString() == "재료 정보를 기입해주세요"){
                    viewModel.setCompareData(
                        v.food_name.text.toString(),
                        location_text,
                        "1111. 11. 11",
                        v.unique_id.text.toString().toInt()
                    )
                }
                else{
                    viewModel.setCompareData(
                        v.food_name.text.toString(),
                        location_text,
                        v.buy_date.text.toString(),
                        v.unique_id.text.toString().toInt()
                    )
                }

                findNavController().navigate(R.id.itemStatusFragment)
            }
        })



        search_food_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.setSearchData(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        back_button.setOnClickListener {
            search_food_edit_text.getText().clear()
            findNavController().navigateUp()
        }


    }

    override fun onAttach(context: Context) { // 프래그먼트에서 뒤로가기 이벤트
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                search_food_edit_text.getText().clear()
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}