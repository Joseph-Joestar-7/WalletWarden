package com.example.walletwarden.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.walletwarden.utils.saveUserData

@Composable
fun LoginScreen(navController: NavController) {
    val context: Context = LocalContext.current
    Surface {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp,16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                var userName by remember { mutableStateOf("") }
                var userBalance by remember { mutableIntStateOf(0) }
                var userWalletBalance by remember { mutableIntStateOf(0) }
                OutlinedTextField(value = userName,
                    onValueChange ={ userName=it },
                    label={Text(text = "Need your username")},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = userBalance.toString(),
                    onValueChange ={ userBalance=it.toInt() },
                    label={Text(text = "Need your current balance")},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = userWalletBalance.toString(),
                    onValueChange ={ userWalletBalance=it.toInt() },
                    label={Text(text = "Need your current wallet balance")},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth())
                Button(onClick ={if(userName.isBlank()||userBalance==0)
                    Toast.makeText(context, "Atleast set up the account properly", Toast.LENGTH_SHORT).show()
                else {
                    saveUserData(context,name=userName, balance = userBalance,
                        wBalance = userWalletBalance)
                    navController.navigate(Screen.Home.route){
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                } })
                {
                    Text(text = "Save and Continue")
                }
            }
        }
    }

}

@Preview
@Composable
fun LoginPrev()
{
    LoginScreen(navController = rememberNavController())
}