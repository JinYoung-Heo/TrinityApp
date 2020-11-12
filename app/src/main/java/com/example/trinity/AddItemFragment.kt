package com.example.trinity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.list_foods.*


class AddItemFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_item, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //플러스 버튼 클릭 : 데이터 추가
        add_food_btn.setOnClickListener {
            val food_name = food_edit_text.text.toString()
            val radio_btn_id = radio_group.checkedRadioButtonId
            var contact = Contacts(0,"null","null")
            val main_activity = activity as MainActivity

            when(radio_btn_id) {
                radio_btn1.id -> {
                    contact = Contacts(0, food_name, "shelf") //Contacts 생성
                    main_activity.addDB(contact)
                    main_activity.addListToAdapter("shelf", 0) //리스트 추가
                }
                radio_btn2.id -> {
                    contact = Contacts(0, food_name, "refrigerator") //Contacts 생성
                    main_activity.addDB(contact)
                    main_activity.addListToAdapter("refrigerator", 1) //리스트 추가
                }
                radio_btn3.id -> {
                    contact = Contacts(0, food_name, "freezer") //Contacts 생성
                    main_activity.addDB(contact)
                    main_activity.addListToAdapter("freezer", 2) //리스트 추가
                }
            }
            findNavController().navigate(R.id.action_addItemFragment_to_mainFragment) // 이동
        }
    }
}