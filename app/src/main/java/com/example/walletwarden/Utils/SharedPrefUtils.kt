package com.example.walletwarden.Utils

import android.content.Context
import android.content.SharedPreferences

fun saveUserData(context: Context, name: String, balance:Int) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("userName", name)
    editor.putInt("userBalance",balance)
    editor.apply()
}