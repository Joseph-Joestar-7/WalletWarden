package com.example.walletwarden

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.walletwarden.database.MonthDatabase
import com.example.walletwarden.database.MonthRepository
import com.example.walletwarden.screens.HomeScreen
import com.example.walletwarden.screens.NavHostScreen
import com.example.walletwarden.ui.theme.WalletWardenTheme
import com.example.walletwarden.viewmodels.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalletWardenTheme {
                val homeViewModel: HomeViewModel = viewModel(
                    factory = HomeViewModel.HomeViewModelFactory(
                        repository = MonthRepository(
                            MonthDatabase.getDatabase(
                                LocalContext.current
                            ).monthDao()
                        )
                    )
                )
                  NavHostScreen(navController=rememberNavController(),homeViewModel, LocalContext.current)
                }
            }
        }
    }

