package com.example.walletwarden.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

fun saveUserData(context: Context, name: String, balance:Int,wBalance:Int) {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("userName", name)
    editor.putInt("userBalance",balance)
    editor.putInt("userWalletBalance",wBalance)
    editor.apply()
}

fun isUserDataAvailable(context: Context): Boolean {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.contains("userName") &&
            sharedPreferences.contains("userBalance") &&
            sharedPreferences.contains("userWalletBalance")
}

fun getUserName(context: Context): String? {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("userName", null)
}

fun getUserBalance(context: Context): Int {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("userBalance", 0)
}

fun getUserWalletBalance(context: Context): Int {
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("userWalletBalance", 0)
}

@SuppressLint("CommitPrefEdits")
fun updateUserName(context:Context, name:String){
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("userName", name)
}

@SuppressLint("CommitPrefEdits")
fun updateUserBalance(context:Context, balance:Int){
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("userBalance", balance)
}

@SuppressLint("CommitPrefEdits")
fun updateUserWalletBalance(context:Context, balance:Int){
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("userWalletBalance", balance)
}