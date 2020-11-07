package com.example.trinity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.list_foods.view.*

class MainFragment : Fragment() {
    var trashcan_state = 0
    var listOfPositionList : MutableList<MutableList<Int>> = mutableListOf(mutableListOf<Int>(), mutableListOf<Int>(), mutableListOf<Int>())
    private val tabTextList = arrayListOf("선반", "냉장", "냉동")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val main_activity = activity as MainActivity
        var adapterList : MutableList<RecyclerViewAdapter> = main_activity.getAdapterList()
//        recycler_view.adapter = adapter // adapter와 recycler view 연동(갱신은 자동으로)
        trashcan_state = 0
        for(i in 0..2) {
            listOfPositionList[i].clear()
        }

        listener(main_activity, adapterList)
        view_pager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
    }

    fun listener(main_activity: MainActivity, adapterList: MutableList<RecyclerViewAdapter>) {
        for(i in 0..2) {
            adapterList[i].setItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    if (trashcan_state == 1) {
                        Log.d("MSG", position.toString())
                        Log.d(
                            "MSG",
                            "Contacts list size = " + main_activity.getContactsListSize(i).toString()
                        )
                        v.check_box.toggle()
                        if (v.check_box.isChecked) {
                            listOfPositionList[i].add(position)
                        } else {
                            listOfPositionList[i].remove(position)
                        }
                        Log.d("MSG", "Integer list size = " + listOfPositionList[i].size.toString())
                    }
                }
            })
        }

        trashcan_btn.setOnClickListener {
            if(trashcan_state == 0) {
                trashcan_btn.setBackgroundColor(Color.parseColor("#ff0000"))
                ContactsViewHolder.activateCheckbox()
                for(i in 0..2) {
                    adapterList[i].notifyDataSetChanged() //리스트뷰 갱신 - 뷰홀더에 있는 bind 함수를 호출하기 위함
                }
                trashcan_state = 1
            }
            else {
                for(i in 0..2) {
//                    listOfPositionList[i].reverse()
                    adapterList[i].setDeleteIdList(listOfPositionList[i])
                }

                main_activity.removeDB()
                main_activity.removeList()

                trashcan_state = 0
                ContactsViewHolder.inActivateCheckbox()
                for(i in 0..2) {
                    listOfPositionList[i].clear()
                }
                trashcan_btn.setBackgroundColor(Color.parseColor("#ffffff"))
                for(i in 0..2) {
                    adapterList[i].notifyDataSetChanged() //리스트뷰 갱신 - 뷰홀더에 있는 bind 함수를 호출하기 위함
                }
            }
        }

        go_to_add_item_page_btn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
        }
    }

}