package com.example.walletwarden.screens

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.walletwarden.R
import com.example.walletwarden.database.MonthDatabase
import com.example.walletwarden.database.MonthEntity
import com.example.walletwarden.database.MonthRepository
import com.example.walletwarden.ui.theme.WalletWardenTheme
import com.example.walletwarden.ui.theme.inversePrimaryDark
import com.example.walletwarden.ui.theme.secondaryContainerLightHighContrast
import com.example.walletwarden.ui.theme.secondaryDark
import com.example.walletwarden.ui.theme.tertiaryDarkMediumContrast
import com.example.walletwarden.ui.theme.tertiaryLight
import com.example.walletwarden.viewmodels.HomeViewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.HomeViewModelFactory(
            repository = MonthRepository(
                MonthDatabase.getDatabase(
                    LocalContext.current
                ).monthDao()
            )
        )
    )
    val context = LocalContext.current

    val allMonths by homeViewModel.allMonths.observeAsState(emptyList())
    val isAdding= remember {
        mutableStateOf(false)
    }
    val inputMonth= remember {
        mutableStateOf("")
    }
    val inputYear= remember {
        mutableStateOf("")
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout {
            val(topBar,balanceRow,cards,add)=createRefs()
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
            BalanceRow(modifier=Modifier.constrainAs(balanceRow){
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
                            OutlinedTextField(
                                value = inputMonth.value,
                                onValueChange = { inputMonth.value = it },
                                label = { Text("Month Name") },
                                modifier = Modifier.fillMaxWidth()
                            )
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
                            if (inputMonth.value!="" && inputYear.value!= "") {
                                val y=inputYear.value.toInt()
                                homeViewModel.addNewMonth(month = inputMonth.value,year=y )
                                inputMonth.value = ""
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
                top.linkTo(balanceRow.bottom)
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
                    if(showEditConfirmationDialog.value)
                    {
                        val editedMonthName = remember { mutableStateOf(monthEntity.month) }
                        val editedYear = remember { mutableStateOf(monthEntity.year.toString()) }
                        AlertDialog(onDismissRequest = { showEditConfirmationDialog.value=false },
                            title = { Text("Oh, you're editing me?") },
                            text={
                                 Column(){
                                     TextField(value =editedMonthName.value ,
                                         onValueChange ={editedMonthName.value=it},
                                         label={Text("Month Name")})
                                     TextField(value =editedYear.value ,
                                         onValueChange ={editedYear.value=it},
                                         label={Text("Month Name")},
                                         keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                                 }
                            },
                            confirmButton = {Button(
                                onClick = {
                                        val y=editedYear.value.toInt()
                                        homeViewModel.editMonth(month = editedMonthName.value,year=y,monthEntity )
//                                        editedMonthName.value = ""
//                                        inputYear.value= ""
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
                        onEdit = {showEditConfirmationDialog.value=true})
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
              onEdit:()->Unit) {
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
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.baseline_arrow_forward_ios_24), contentDescription = "Forward")
                    }
                }
            }

        }
    }

}

@Composable
fun BalanceRow(modifier: Modifier = Modifier) {

    val balance= remember {
        mutableStateOf("0")
    }
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
                Text(text= stringResource(R.string.Current_Balance))
                Text(text = balance.value)
        }
    }
}





@Preview
@Composable
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    WalletWardenTheme {
        HomeScreen()
    }
}