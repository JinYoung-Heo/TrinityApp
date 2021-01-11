package com.mypackage.naengbiseo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**데이터를 저장할 Entity, ROOM(MODEL) 관련 클래스.
 */


@Entity(
    tableName = "excelData",
    indices = [Index(value = ["아이콘종류", "보관방법", "소비기한", "처리방법"], unique = true)]
) // 이걸로 푸드데이터의 유니크를 정할 수 있다!

data class ExcelData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 기본값
    @ColumnInfo(name = "아이콘종류") var iconName: String = "",
    @ColumnInfo(name = "보관방법") var storeWay: String = "", // 기본값
    @ColumnInfo(name = "소비기한") var useDate: String = "",
    @ColumnInfo(name = "처리방법") var treatWay: String = ""
)
