package com.example.walletwarden.database

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val monthId:Int,
    @DrawableRes
    val icon:Int,
    val name: String,
    val date: Date,
    val amount: Int,
    val isExpense: Boolean,
)