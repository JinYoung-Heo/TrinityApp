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
import com.google.android.material.tabs.TabLayoutMediator
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

        trashcan_state = 0
        for(i in 0..2) {
            listOfPositionList[i].clear()
        }

        listener(main_activity, adapterList)

        view_pager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(tabLayoutId, view_pager){ tab, position ->
            tab.text = tabTextList[position]
        }.attach()
    }

    fun listener(main_activity: MainActivity, adapterList: MutableList<RecyclerViewAdapter>) {
        for(i in 0..2) {
            adapterList[i].setItemClickListener(object : RecyclerViewAdapter.OnItemClickListener {
                override fun onClick(v: View, position: Int) {
//                    Toast.makeText(context, position.toString(), Toast.LENGTH_SHORT).show()
                    if (trashcan_state == 1) {
                        v.check_box.toggle()
                        if (v.check_box.isChecked) {
                            listOfPositionList[i].add(position)
                        } else {
                            listOfPositionList[i].remove(position)
                        }
                    }
                }
            })
        }

        trashcan_btn.setOnClickListener {
            if(trashcan_state == 0) {
                trashcan_btn.setBackgroundColor(Color.parseColor("#ff0000"))
                ContactsViewHolder.activateCheckbox()
                for(i in 0..2) {
                    adapterList[i].notifyDataSetChanged() // Checkbox를 표시하기위함
                }
                trashcan_state = 1
            }
            else {
                for(i in 0..2) {
                    Log.d("MSG", i.toString() + ": " + listOfPositionList[i].toString())
                    adapterList[i].setDeleteIdList(listOfPositionList[i])
                }

                main_activity.removeDB()

                trashcan_state = 0
                ContactsViewHolder.inActivateCheckbox()
                for(i in 0..2) {
                    listOfPositionList[i].clear()
                }
                trashcan_btn.setBackgroundColor(Color.parseColor("#ffffff"))
                for(i in 0..2) {
                    adapterList[i].clearFoodId()
                    adapterList[i].notifyDataSetChanged() // checkbox 다시 안보이도록 하기위해
                }
            }
        }

        go_to_add_item_page_btn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addItemFragment)
        }
    }

}