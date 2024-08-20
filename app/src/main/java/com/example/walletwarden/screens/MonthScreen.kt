package com.example.walletwarden.screens

import android.view.Surface
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.walletwarden.R
import com.example.walletwarden.database.ExpenseRepository
import com.example.walletwarden.ui.theme.inversePrimaryDark
import com.example.walletwarden.viewmodels.MonthScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthScreen(navController: NavHostController, monthId: Int) {
    val context = LocalContext.current
    val viewModel: MonthScreenViewModel = viewModel(
        factory = MonthScreenViewModel.MonthScreenViewModelFactory(context, monthId)
    )

    Surface(modifier=Modifier.fillMaxSize())
    {
        ConstraintLayout {
            val(topBar,balanceRow,cards,add)=createRefs()
            CenterAlignedTopAppBar(title = {
                Row {
                    Text(text = )
                } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor =
                ), modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}

