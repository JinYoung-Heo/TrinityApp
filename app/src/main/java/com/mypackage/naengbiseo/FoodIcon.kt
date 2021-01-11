package com.mypackage.naengbiseo

class FoodIcon(val iconName: String, val iconResource: Int, val category: String = "") {
    val getIconName: String
        get() {
            return iconName
        }
    val getIconResource: Int
        get() {
            return iconResource
        }

    val getCategory:String
        get(){
            return category
        }
}