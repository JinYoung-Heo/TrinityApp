package com.mypackage.naengbiseo

class DelData(
    val foodName: String,
    val storeLocation: String,
    val buyDate: String,
    val uniqueId: Int
) {
    val getFoodName: String
        get() {
            return foodName
        }
    val getStoreLocation: String
        get() {
            return storeLocation
        }

    val getBuyDate: String
        get() {
            return buyDate
        }

    val getUniqueId: Int
        get() {
            return uniqueId
        }


}