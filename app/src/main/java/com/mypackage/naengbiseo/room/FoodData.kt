package com.mypackage.naengbiseo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**데이터를 저장할 Entity, ROOM(MODEL) 관련 클래스.
 */



@Entity(tableName = "foodData",indices = [Index(value = ["재료명", "보관장소","구매일자","구매유무","헤더","고유아이디"], unique = true)]) // 이걸로 푸드데이터의 유니크를 정할 수 있다!

data class FoodData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 기본값
    @ColumnInfo(name = "재료명") var foodName: String = "",
    @ColumnInfo(name = "보관장소") var storeLocation: String = "",
    @ColumnInfo(name = "아이콘") var foodIcon: Int = 0,
    @ColumnInfo(name = "구매유무") var purchaseStatus: Int = 1, // 기본값
    @ColumnInfo(name = "카테고리") var foodCategory: String = "",
    @ColumnInfo(name = "수량") var foodNumber: Int = 0,
    @ColumnInfo(name = "메모") var foodMemo: String = "", // 기본값
    @ColumnInfo(name = "보관방법") var storeWay: String = "", // 기본값
    @ColumnInfo(name = "처리방법") var treatWay: String = "", // 기본값
    @ColumnInfo(name = "유통기한") var expirationDate: String = "",
    @ColumnInfo(name = "소비기한") var useDate: String = "",
    @ColumnInfo(name = "구매일자") var buyDate: String = "",
    @ColumnInfo(name = "헤더") var header: Int = 0,
    @ColumnInfo(name = "없음") var Null: Int = 0,
    @ColumnInfo(name = "고유아이디") var uniqueId: Int = 0
)
