package com.example.walletwarden.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.walletwarden.ui.theme.tertiaryLight
import com.example.walletwarden.viewmodels.HomeViewModel
import com.example.walletwarden.viewmodels.MonthScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthScreen(navController: NavHostController, monthId: Int, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val viewModel: MonthScreenViewModel = viewModel(
        factory = MonthScreenViewModel.MonthScreenViewModelFactory(context, monthId)
    )

    Surface(modifier=Modifier.fillMaxSize())
    {
        ConstraintLayout {
            val(topBar,balanceRow,cards,add)=createRefs()
//            var monthName by remember { mutableStateOf<String?>(null) }
//            var year by remember { mutableStateOf<Int?>(null) }
//
//            // Load monthName and year using LaunchedEffect
//            LaunchedEffect(monthId) {
//                monthName = viewModel.getMonthName().also {
//                    println("Month name fetched: $it")
//                }
//                year = viewModel.getYear().also {
//                    println("Year fetched: $it")
//                }
//            }
            var monthName by remember { mutableStateOf<String?>(null) }
            var year by remember { mutableStateOf<Int?>(null) }

            LaunchedEffect(monthId) {
                monthName = homeViewModel.getMonthName(monthId)
                year = homeViewModel.getYear(monthId)
            }
            CenterAlignedTopAppBar(
                title = {
                Row {
                    if (monthName != null && year != null) {
                        Text(text = "$monthName $year")
                    } else {
                        Text(text = "Loading...")
                    }
                }
                        },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = tertiaryLight
                ), modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}

