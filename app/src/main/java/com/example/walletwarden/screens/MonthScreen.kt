package com.example.walletwarden.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.walletwarden.R
import com.example.walletwarden.database.ExpenseEntity
import com.example.walletwarden.ui.theme.tertiaryLight
import com.example.walletwarden.viewmodels.HomeViewModel
import com.example.walletwarden.viewmodels.MonthScreenViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthScreen(navController: NavHostController, monthId: Int, homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val viewModel: MonthScreenViewModel = viewModel(
        factory = MonthScreenViewModel.MonthScreenViewModelFactory(context, monthId)
    )
    val allExpenses by viewModel.expenses.observeAsState()
    val monthlyBalance:Int=0

    val expenseMap= mapOf(
        "Food" to R.drawable.icon_food,
        "Transport" to R.drawable.icon_transport,
        "Housing" to R.drawable.icon_housing,
        "Entertainment" to R.drawable.icon_entertainment,
        "Personal Care" to R.drawable.icon_personalcare,
        "Health Care" to R.drawable.icon_healthcare,
        "Travel" to R.drawable.icon_travel,
        "Banking" to R.drawable.icon_bank,
        "Groceries" to R.drawable.icon_groceries,
        "Shopping" to R.drawable.icon_shopping,
    )

    Surface(modifier=Modifier.fillMaxSize())
    {
        ConstraintLayout {
            val(topBar,balanceRow,cards,add)=createRefs()

            var monthName by remember { mutableStateOf<String?>(null) }
            var year by remember { mutableStateOf<Int?>(null) }

            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            var showBottomSheet by remember { mutableStateOf(false) }
            var isAddingExpense by remember{ mutableStateOf(false) }
            var isAddingIncome by remember{ mutableStateOf(false) }

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
            if (showBottomSheet){
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ){
                    Column {
                        TextButton(onClick = { isAddingExpense = true
                            scope.launch { showBottomSheet=false} }) {
                            Text(text = "Add Expense")
                        }
                        TextButton(onClick = { isAddingIncome = true
                            scope.launch { showBottomSheet=false } }) {
                            Text(text = "Add Income")
                        }
                    }
                }
            }
            if(isAddingIncome || isAddingExpense)
            {
                val expanded = remember { mutableStateOf(false) }
                var itemName by remember { mutableStateOf("") }
                var expenseType by remember { mutableStateOf("") }
                var amt by remember { mutableStateOf("") }

                AlertDialog(onDismissRequest = { isAddingIncome=false
                                               isAddingExpense=false},
                    title = {
                        Text(text = if(isAddingIncome) "Ayo Congo you got an income?"
                        else "Blud can't stop spending")
                    },
                    text = {
                        Column {
                            OutlinedTextField(
                                value =itemName ,
                                onValueChange = {itemName=it},
                                label = { Text("ADD ITEM NAME") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Text
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if(isAddingExpense)
                            {
                                Box {
                                    TextButton(onClick = { expanded.value = true }) {
                                        Text(text =  "Choose the type of Expense")
                                    }
                                    DropdownMenu(expanded = expanded.value,
                                        onDismissRequest = {expanded.value = false}) {
                                        expenseMap.keys.forEach {expense->
                                            DropdownMenuItem(
                                                text = { Text(text =expense) },
                                                onClick = {
                                                    expenseType= expense
                                                    expanded.value = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            OutlinedTextField(
                                value =amt ,
                                onValueChange = {amt=it},
                                label = { Text("ADD AMOUNT") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                        }
                    },
                    confirmButton = {Button(
                        onClick = {
                            if(expenseType.isBlank()) {
                                scope.launch {
                                    Toast.makeText(context, "Enter the type of expense, Baka", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            else{
                            scope.launch {
                            val amount=amt.toInt()
                            var icon:Int=0
                            icon = if(isAddingExpense)
                                expenseMap[expenseType]!!
                            else
                                R.drawable.icon_rupees
                            viewModel.addExpense(
                                name=itemName,
                                date=Date(),
                                amount=amount,
                                icon=icon,
                                isExpense = isAddingExpense
                            )
                            isAddingIncome=false
                            isAddingExpense=false
                        }
                            }

                        }
                    ){
                        Text(text="ADD")
                    } },
                    dismissButton = { Button(onClick = {
                        isAddingIncome = false
                        isAddingExpense = false
                    }) {
                        Text(text = "CANCEL")
                    }})
            }
            FloatingActionButton(modifier= Modifier
                .padding(8.dp)
                .constrainAs(add) {
                    top.linkTo(cards.bottom)
                    end.linkTo(parent.end)
                },onClick = {showBottomSheet = true}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }

       }
    }
}

@Composable
fun ExpenseItem(expenseEntity: ExpenseEntity,
                onDelete:()->Unit,
                onEdit:()->Unit,){

    Card(modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,)
            {
                Image(painter = painterResource(expenseEntity.icon) , contentDescription =null,
                    modifier = Modifier.size(64.dp))
                Column {
                    Text(text= expenseEntity.name,
                        color = if(expenseEntity.isExpense) Color.Red
                    else Color.Green)
                    Text(text = expenseEntity.amount.toString())
                }
                Column {
                    IconButton(onClick = onEdit) {
                        Icon(painter = painterResource(R.drawable.baseline_create_24 ), contentDescription =null )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(painter = painterResource(R.drawable.baseline_delete_24 ), contentDescription =null )
                    }
                }
            }
        }
    }
}


