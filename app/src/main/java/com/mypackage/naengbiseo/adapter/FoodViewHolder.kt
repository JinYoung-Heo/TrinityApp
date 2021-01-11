package com.mypackage.naengbiseo.adapter

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.food_item.view.buy_date
import kotlinx.android.synthetic.main.food_item.view.check_box
import kotlinx.android.synthetic.main.food_item.view.d_day
import kotlinx.android.synthetic.main.food_item.view.food_icon
import kotlinx.android.synthetic.main.food_item.view.food_name
import kotlinx.android.synthetic.main.food_item.view.food_number
import kotlinx.android.synthetic.main.food_item.view.unique_id
import kotlinx.android.synthetic.main.main_header.view.*
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*

class FoodViewHolder(v: View):RecyclerView.ViewHolder(v) {

    companion object { // companion object는 JAVA로 치면 static
        private var checkbox_state = 0
        fun activateCheckbox() {
            checkbox_state = 1
        }
        fun inActivateCheckbox() {
            checkbox_state = 0
        }
    }
    var view : View = v

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(foodData: FoodData, position: Int) {
        if(foodData.header == 0 && foodData.Null != 1){
            view.d_day.setTextColor(Color.parseColor("#666666"))
            //var simpleFormat2= SimpleDateFormat("yyyy. MM. dd")
            var uniqueId = foodData.uniqueId.toString()

            view.unique_id.setText(uniqueId)

            var foodicon = foodData.foodIcon
            view.food_icon.setImageResource(foodicon)

            var simpleFormat= SimpleDateFormat("yyyy년 MM월 dd일")
            var simpleFormat2= SimpleDateFormat("yyyy. MM. dd")

            var realExpDate =simpleFormat.parse(foodData.expirationDate) // 문자열로 부터 날짜 들고오기!

            var realBuyDate = simpleFormat.parse(foodData.buyDate)
            var dateString = simpleFormat2.format(realBuyDate)

            var today = Calendar.getInstance() // 현재 날짜
            var dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)

            var dDayText:String
            if(dDay>0) {
                view.food_icon.alpha = 0.6F // 이미지뷰의 투명도 조정.
                dDayText= "D+" + abs(dDay).toString()
                view.d_day.setTextColor(Color.parseColor("#fb343e"))
            }
            else if(dDay<0) {
                view.food_icon.alpha = 1F
                dDayText= "D-" + abs(dDay-1).toString()
                if(abs(dDay-1) < 4) {
                    view.food_icon.alpha = 0.6F
                    view.d_day.setTextColor(Color.parseColor("#fb343e"))
                }
            }
            else {
                view.food_icon.alpha = 0.6F
                dDayText="D-day"
                view.d_day.setTextColor(Color.parseColor("#fb343e"))
            }
            view.food_name.setText(foodData.foodName)
            view.food_number.setText(foodData.foodNumber.toString())
            if(foodData.buyDate == "1111년 11월 11일"){
                view.food_icon.alpha = 1F
                view.buy_date.setText("재료 정보를 기입해주세요")
                view.d_day.setText("")
            }
            else{
                view.buy_date.setText(dateString)
                view.d_day.setText(dDayText)
            }



            // 체크박스 관련
            if(checkbox_state == 1) {
                view.check_box.visibility = View.VISIBLE
                view.check_box.setChecked(false)
            }
            else {
                view.check_box.visibility = View.GONE
            }
        }

        if(foodData.header == 1 && foodData.Null != 1){
            if(foodData.foodCategory != ""){
                view.header_name.text = foodData.foodCategory
            }
            else if(foodData.buyDate != ""){
                if(foodData.buyDate == "1111년 11월 11일"){
                    view.header_name.text = "재료정보 입력 요"
                }
                else{
                    view.header_name.text = foodData.buyDate
                }
            }
        }
    }


}