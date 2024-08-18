package com.example.walletwarden.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    var month:String,
    var year :Int,
    var monthlyexp:Int
)