package com.mypackage.naengbiseo.adapter

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mypackage.naengbiseo.room.FoodData
import kotlinx.android.synthetic.main.food_item.view.buy_date
import kotlinx.android.synthetic.main.food_item.view.d_day
import kotlinx.android.synthetic.main.food_item.view.food_icon
import kotlinx.android.synthetic.main.food_item.view.food_name
import kotlinx.android.synthetic.main.food_item.view.food_number
import kotlinx.android.synthetic.main.food_item_search_version.view.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmViewHolder(v: View): RecyclerView.ViewHolder(v) {

    var view : View = v

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(foodData: FoodData, dDay: Long) {
        view.d_day.setTextColor(Color.parseColor("#666666"))
        view.food_icon.setImageResource(foodData.foodIcon)
        var alpha = view.food_icon.drawable
        var simpleFormat= SimpleDateFormat("yyyy�� MM�� dd��")
        var simpleFormat2= SimpleDateFormat("yyyy. MM. dd")

        var realExpDate =simpleFormat.parse(foodData.expirationDate) // ���ڿ��� ���� ��¥ ������!

        var realBuyDate = simpleFormat.parse(foodData.buyDate)
        var dateString = simpleFormat2.format(realBuyDate)

        var today = Calendar.getInstance() // ���� ��¥
        var dDay = (today.time.time - realExpDate.time) / (60 * 60 * 24 * 1000)

        var dDayText: String
        if (dDay > 0) {
            //alpha.alpha = 153
            view.food_icon.alpha = 0.6F
            dDayText = "D+" + Math.abs(dDay).toString()
            view.d_day.setTextColor(Color.parseColor("#fb343e"))
        } else if (dDay < 0) {
            //alpha.alpha = 255
            view.food_icon.alpha = 1.0F
            dDayText = "D-" + Math.abs(dDay - 1).toString()
            if (Math.abs(dDay - 1) < 4) {
                view.food_icon.alpha = 0.6F
                view.d_day.setTextColor(Color.parseColor("#fb343e"))
            }
        } else {
            //alpha.alpha = 153
            view.food_icon.alpha = 0.6F
            dDayText = "D-day"
            view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        when(foodData.storeLocation){
            "shelf" -> view.slt.setText("����")
            "cool" -> view.slt.setText("����")
            "cold" -> view.slt.setText("�õ�")
        }

        view.food_name.setText(foodData.foodName)
        view.food_number.setText(foodData.foodNumber.toString())
        if(foodData.buyDate == "1111�� 11�� 11��"){
            view.buy_date.setText("��� ������ �������ּ���")
            view.d_day.setText("")
        }
        else{
            view.buy_date.setText(dateString)
            view.d_day.setText(dDayText)
        }
        /*var dDayText:String
        if(dDay>0) {
            dDayText= "D+" + Math.abs(dDay).toString()
            view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        else if(dDay<0) {
            dDayText= "D-" + Math.abs(dDay - 1).toString()
            if(Math.abs(dDay) <=3) view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        else {
            dDayText="D-day"
            view.d_day.setTextColor(Color.parseColor("#fb343e"))
        }
        view.food_name.setText(foodData.foodName)
        view.food_number.setText(foodData.foodNumber.toString())
        view.buy_date.setText(foodData.buyDate)
        view.d_day.setText(dDayText)
        view.food_icon.setImageResource(foodData.foodIcon)*/
    }
}