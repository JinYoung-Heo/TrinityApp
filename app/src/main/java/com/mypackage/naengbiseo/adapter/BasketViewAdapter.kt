package com.mypackage.naengbiseo.adapter

import android.app.AlertDialog
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.MainActivity
import com.mypackage.naengbiseo.R
import com.mypackage.naengbiseo.viewmodel.BasketViewModel
import kotlinx.android.synthetic.main.basket_custom_dialog.view.*
import kotlinx.android.synthetic.main.basket_empty_layout.view.*
import kotlinx.android.synthetic.main.basket_food_item.view.*

class BasketViewAdapter(private val viewModel: BasketViewModel) :
    RecyclerView.Adapter<BasketViewHolder>() {
    private val TYPE_BASKET_EMPTY = 0
    private val TYPE_BASKET_NOT_EMPTY = 1
    override fun getItemCount(): Int {
        if (viewModel.basketFoodList.isEmpty()) {
            return 1
        }
        return viewModel.basketFoodList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (viewModel.basketFoodList.isEmpty()) {
            return TYPE_BASKET_EMPTY
        }
        return TYPE_BASKET_NOT_EMPTY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        var myLayout: Int
        when (viewType) {
            TYPE_BASKET_EMPTY -> myLayout = R.layout.basket_empty_layout
            TYPE_BASKET_NOT_EMPTY -> myLayout = R.layout.basket_food_item
            else -> myLayout = R.layout.fragment_error
        }
        return BasketViewHolder(
            LayoutInflater.from(parent.context).inflate(myLayout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        if (viewModel.basketFoodList.isEmpty()) {
            holder.view.foodAddButtonInBasket.setOnClickListener {
                viewModel.foodAddButtonClicked(true)
            }
        } else {
            val foodData = viewModel.getBasketFoodAt(position)
            holder.bind(foodData, position)

            holder.view.buyButton.setOnClickListener {
                Log.d("위치", position.toString())
                Log.d("MSG", "food list size: " + viewModel.basketFoodList.size.toString())
                val itemView = holder.view
                val storeLocation: String
                val radio_btn_id = itemView.radio_group.checkedRadioButtonId

                when (radio_btn_id) {
                    itemView.radio_btn1.id -> {
                        storeLocation = "선반"
                    }
                    itemView.radio_btn2.id -> {
                        storeLocation = "냉장"
                    }
                    itemView.radio_btn3.id -> {
                        storeLocation = "냉동"
                    }
                    else -> {
                        storeLocation = "에러"
                    }
                }
                // 구매 완료 상태로 바꾸기
                viewModel.basketFoodList[position].purchaseStatus = 1

                // 지금까지 수정했던 값들 업데이트 - basketFragment에서 updateLiveData observe
                viewModel.setUpdateLiveData(true)

                val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.basket_custom_dialog, null)
                dialogView.basketDialogTextView.text = MainActivity.pref_user_name.myEditText + "님, " + storeLocation + "보관 해두었습니다:>"
                //AlertDialogBuilder
                val builder = AlertDialog.Builder(itemView.context)
                    .setView(dialogView)
                    .setCancelable(false)
                //show dialog
                val alertDialog = builder.show()
                delayTime(1000, alertDialog)
            }
            holder.view.minusBtn.setOnClickListener {
                val itemView = holder.view
                var quantity = itemView.quantityTextView.text.toString().toInt()
                if (quantity > 1) {
                    quantity--
                    itemView.quantityTextView.setText((quantity).toString())
                }
                viewModel.basketFoodList[position].foodNumber = quantity
            }
            holder.view.plusBtn.setOnClickListener {
                val itemView = holder.view
                var quantity = itemView.quantityTextView.text.toString().toInt()
                quantity++
                itemView.quantityTextView.setText((quantity).toString())
                viewModel.basketFoodList[position].foodNumber = quantity
            }
            holder.view.radio_group.setOnCheckedChangeListener { radioGroup, checkedId ->
                val itemView = holder.view
                val storeLocation: String
                when (checkedId) {
                    itemView.radio_btn1.id -> {
                        storeLocation = "shelf"
                    }
                    itemView.radio_btn2.id -> {
                        storeLocation = "cool"
                    }
                    itemView.radio_btn3.id -> {
                        storeLocation = "cold"
                    }
                    else -> {
                        storeLocation = "error"
                    }
                }
                viewModel.basketFoodList[position].storeLocation = storeLocation
            }
        }
    }

    //    ClickListener
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun delayTime(time: Long, d: AlertDialog) {

        Handler().postDelayed({
            d.dismiss()
        }, time)

    }
}