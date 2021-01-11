package com.mypackage.naengbiseo

class AddData(
    val foodIcon: Int,
    val iconName: String,
    val category: String,
    val storeWay: String,
    val useData: String,
    val treatWay: String
) {
    val getIconName: String
        get() {
            return iconName
        }
    val getfoodIcon: Int
        get() {
            return foodIcon
        }

    val getCategory: String
        get() {
            return category
        }

    val getStoreWay: String
        get() {
            return storeWay
        }

    val getUseDate: String
        get() {
            return useData
        }

    val getTreatWay: String
        get() {
            return treatWay
        }
}