package com.example.trinity

import androidx.room.*

@Entity(tableName = "tb_contacts")
data class Contacts(
    @PrimaryKey(autoGenerate = true) val id:Long,
    var name: String,
    var location: String
)

