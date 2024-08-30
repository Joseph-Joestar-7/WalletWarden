package com.example.walletwarden.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.walletwarden.R
import com.example.walletwarden.database.MonthDao
import com.example.walletwarden.database.MonthDatabase
import com.example.walletwarden.database.MonthEntity
import com.example.walletwarden.database.MonthRepository
import com.example.walletwarden.ui.theme.WalletWardenTheme
import com.example.walletwarden.ui.theme.inversePrimaryDark
import com.example.walletwarden.ui.theme.secondaryContainerLightHighContrast
import com.example.walletwarden.ui.theme.secondaryDark
import com.example.walletwarden.ui.theme.tertiaryDarkMediumContrast
import com.example.walletwarden.ui.theme.tertiaryLight
import com.example.walletwarden.utils.getUserBalance
import com.example.walletwarden.utils.getUserName
import com.example.walletwarden.utils.getUserWalletBalance
import com.example.walletwarden.viewmodels.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val allMonths by homeViewModel.allMonths.observeAsState(emptyList())
    val isAdding= remember {
        mutableStateOf(false)
    }
    val scope= rememberCoroutineScope()
    val expanded = remember { mutableStateOf(false) }
    val selectedMonth = remember { mutableStateOf("") }
    val selectedMonthNo = remember { mutableIntStateOf(0) }
    val monthMap = mapOf(
        "January" to 1, "February" to 2, "March" to 3, "April" to 4,
        "May" to 5, "June" to 6, "July" to 7, "August" to 8,
        "September" to 9, "October" to 10, "November" to 11, "December" to 12
    )
    val inputYear= remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout {
            val(topBar,detailsRow,cards,add)=createRefs()
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(R.string.app_name),
                    style =MaterialTheme.typography.displayMedium,
                    fontSize = 40.sp) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = inversePrimaryDark
                ), modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            DetailsRow(context=context,modifier=Modifier.constrainAs(detailsRow){
                top.linkTo(topBar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            if(isAdding.value)
            {
                AlertDialog(
                    onDismissRequest = {isAdding.value=false},
                    title = { Text(text = "Add New Month") },
                    text = {
                        Column {
                            Box {
                                TextButton(onClick = { expanded.value = true }) {
                                    Text(text = selectedMonth.value.ifEmpty { "Choose Month" })
                                }
                                DropdownMenu(
                                    expanded = expanded.value,
                                    onDismissRequest = { expanded.value = false }
                                ){
                                    monthMap.keys.forEach { month ->
                                        DropdownMenuItem(
                                            text = { Text(text = month) },
                                            onClick = {
                                                selectedMonth.value = month
                                                selectedMonthNo.value= monthMap[month]!!
                                                expanded.value = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = inputYear.value,
                                onValueChange = { inputYear.value = it },
                                label = { Text("Year") },
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Number
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {Button(
                        onClick = {
                            if (selectedMonth.value!="" && inputYear.value!= "") {
                                val y=inputYear.value.toInt()
                                homeViewModel.addNewMonth(month = selectedMonth.value,year=y, moNo = selectedMonthNo.value )
                                selectedMonth.value = ""
                                inputYear.value= ""
                                isAdding.value = false
                            } else {
                                Toast.makeText(
                                    context,
                                    "Proper Input where,bro?",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text("Add")
                    }
                    },
                    dismissButton = {
                        TextButton(onClick = { isAdding.value = false }) {
                            Text("Cancel")
                        }
                    })
            }
            LazyColumn(modifier=Modifier.constrainAs(cards){
                top.linkTo(detailsRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                items(allMonths) { monthEntity ->
                    val showDeleteConfirmationDialog = remember { mutableStateOf(false) }
                    if (showDeleteConfirmationDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                showDeleteConfirmationDialog.value = false
                            },
                            title = { Text("You're about to delete this month") },
                            text = { Text("But would you reconsider your decision?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        homeViewModel.deleteMonth(monthEntity)
                                        showDeleteConfirmationDialog.value = false
                                    }
                                ) {
                                    Text("Nah, I'd Delete")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showDeleteConfirmationDialog.value = false }
                                ) {
                                    Text("Nigerundayoo")
                                }
                            }
                        )
                    }
                    val showEditConfirmationDialog = remember { mutableStateOf(false) }
                    val editedMonthName = remember { mutableStateOf(monthEntity.month) }
                    val editedYear = remember { mutableStateOf(monthEntity.year.toString())}
                    if(showEditConfirmationDialog.value)
                    {
                        AlertDialog(onDismissRequest = { showEditConfirmationDialog.value=false },
                            title = { Text("Oh, you're editing me?") },
                            text={
                                 Column(){
                                     Box {
                                         TextButton(onClick = { expanded.value = true }) {
                                             Text(text =  "Choose the new month")
                                         }
                                         DropdownMenu(
                                             expanded = expanded.value,
                                             onDismissRequest = { expanded.value = false }
                                         ){
                                             monthMap.keys.forEach { month ->
                                                 DropdownMenuItem(
                                                     text = { Text(text = month) },
                                                     onClick = {
                                                         editedMonthName.value = month
                                                         expanded.value = false
                                                     }
                                                 )
                                             }
                                         }
                                     }
                                     TextField(value =editedYear.value ,
                                         onValueChange ={editedYear.value=it},
                                         label={Text("Input new year")},
                                         keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                 }
                            },
                            confirmButton = {Button(
                                onClick = {
                                        val y=editedYear.value.toInt()
                                    monthMap[editedMonthName.value]?.let {
                                        homeViewModel.editMonth(month = editedMonthName.value,year=y,
                                            moNo = it,monthEntity )
                                    }
                                       editedMonthName.value = ""
                                     editedYear.value= ""
                                        showEditConfirmationDialog.value = false
                                }
                            ) {
                                Text("Confirm edit")
                            }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showEditConfirmationDialog.value = false }
                                ) {
                                    Text("Nah, forget editing")
                                }
                            })
                    }

                    MonthItem(monthEntity = monthEntity,
                        onDelete = {showDeleteConfirmationDialog.value=true},
                        onEdit = {showEditConfirmationDialog.value=true},
                        onForward = {
                            scope.launch {
                                navController.navigate(Screen.MonthExpense.createRoute(monthEntity.id))
                            }
                        })
                }
            }
            FloatingActionButton(modifier= Modifier
                .padding(8.dp)
                .constrainAs(add) {
                    top.linkTo(cards.bottom)
                    end.linkTo(parent.end)
                },onClick = {isAdding.value=true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    }
}

@Composable
fun MonthItem(monthEntity: MonthEntity,
              onDelete:()->Unit,
              onEdit:()->Unit,
              onForward:()->Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(color = tertiaryLight)
        )
    {
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
                Column(modifier = Modifier.padding(16.dp))
                {
                    Text(text = monthEntity.month+" "+monthEntity.year)

                    Text(text = monthEntity.monthlyexp.toString())
                }
                Row(){
                    IconButton(onClick = onDelete) {
                        Icon(painter = painterResource(R.drawable.baseline_delete_24), contentDescription = "Delete")
                    }
                    IconButton(onClick = onEdit) {
                        Icon(painter = painterResource(R.drawable.baseline_create_24), contentDescription = "Edit")
                    }
                    IconButton(onClick =onForward) {
                        Icon(painter = painterResource(R.drawable.baseline_arrow_forward_ios_24), contentDescription = "Forward")
                    }
                }
            }

        }
    }

}

@Composable
fun DetailsRow(modifier: Modifier = Modifier,context: Context) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
                Text(text="Welcome ${getUserName(context)}")
                Text(text ="Your balance: ${getUserBalance(context)}")
            Text(text="Your wallet balance: ${getUserWalletBalance(context)}")
        }
    }
}
