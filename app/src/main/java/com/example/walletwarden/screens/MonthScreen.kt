package com.example.walletwarden.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.walletwarden.database.ExpenseEntity
import com.example.walletwarden.ui.theme.WalletWardenTheme
import com.example.walletwarden.ui.theme.tertiaryLight
import com.example.walletwarden.viewmodels.HomeViewModel
import com.example.walletwarden.viewmodels.MonthScreenViewModel
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthScreen(navController: NavHostController, monthId: Int, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val viewModel: MonthScreenViewModel = viewModel(
        factory = MonthScreenViewModel.MonthScreenViewModelFactory(context, monthId)
    )
    val allExpenses by viewModel.expenses.observeAsState()
    val monthlyBalance:Int=0
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
            BalanceRow(balance = monthlyBalance, modifier = Modifier.constrainAs(balanceRow){
                top.linkTo(topBar.bottom, margin = 10.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            LazyColumn(modifier=Modifier.constrainAs(cards){
                top.linkTo(balanceRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }){
                items(allExpenses?: emptyList()){ ExpenseEntity->
                    ExpenseItem(expenseEntity =ExpenseEntity,
                        onDelete = { viewModel.deleteExpense(ExpenseEntity.id) },
                        onEdit = {})
                }
                


            }
       }
    }
}

@Composable
fun ExpenseItem(expenseEntity: ExpenseEntity,
                onDelete:()->Unit,
                onEdit:()->Unit,
                ) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(color = tertiaryLight)
    ){
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()){
                Image(painter = , contentDescription = )
                Column {
                    Text(text= expenseEntity.name)
                    Text(text = expenseEntity.amount.toString())
                }
                Column {
                    IconButton(onClick = onEdit) {
                        Icon(painter = , contentDescription = )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(painter = , contentDescription = )
                    }
                }

            }
        }
    }

}

