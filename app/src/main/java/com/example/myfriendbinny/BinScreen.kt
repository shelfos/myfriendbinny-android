package com.example.myfriendbinny

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfriendbinny.model.BinData
import com.example.myfriendbinny.model.BinDataStore
import com.example.myfriendbinny.model.ScreenStates
import com.example.myfriendbinny.ui.theme.AppScaffold
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun BinItem(
    bin: BinData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = bin.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(bottom = 2.dp),
            color = MaterialTheme.colorScheme.primary //Color.Yellow
        )
        Text(
            text = bin.description,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            modifier = modifier
                .padding(bottom = 5.dp, start = 10.dp)
        )
    }
}

@Composable
fun BinList(
    bins: List<BinData>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        items(bins) { currentBin ->
            BinItem(bin = currentBin)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BinScreen(
    modifier: Modifier = Modifier
) {
    //For remembering the screen state to render the screen accordingly
    val screenState = remember { mutableStateOf(ScreenStates.BIN_LIST) }

    // Initialize DataStore flow and coroutine scope
    val binsFlow = BinDataStore.readBins(LocalContext.current)
    val bins by binsFlow.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    AppScaffold(
        showPlus = screenState.value == ScreenStates.BIN_LIST,
        onPlusClick = { screenState.value = ScreenStates.BIN_ADD }
    ) {
        when (screenState.value) {
            ScreenStates.BIN_LIST -> {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Text(
                        text = "Bin List",
                        modifier = Modifier.padding(top = 150.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp
                    )
                    BinList(
                        //sampleBinsList,
                        bins = bins,
                        modifier = Modifier.padding(top = 12.dp, bottom = 50.dp)
                    )
                }
            }

            ScreenStates.BIN_ADD -> {
                BackHandler {
                    screenState.value = ScreenStates.BIN_LIST
                }
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    var binName by rememberSaveable { mutableStateOf("") }
                    var binDescription by rememberSaveable { mutableStateOf("") }

                    Text(
                        text = "Add New Bin",
                        modifier = Modifier.padding(top = 150.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    NameField(name = binName, onNameChange = { binName = it})

                    Spacer(modifier = Modifier.height(16.dp))

                    DescriptionField(
                        description = binDescription,
                        onDescriptionChange = { binDescription = it}
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val context = LocalContext.current
                    Row {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            onClick = {
                                //TODO: validation
                                val newBin = BinData(
                                    id = UUID.randomUUID().toString(),
                                    name = binName,
                                    description = binDescription
                                )

                                // Save new bin using DataStore
                                coroutineScope.launch {
                                    val updatedBins = bins + newBin
                                    BinDataStore.saveBins(context, updatedBins)
                                    screenState.value = ScreenStates.BIN_LIST
                                }
                            }
                        ) {
                            Text(
                                text = "SUBMIT",
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.5.sp
                            )
                        }

                    }

                }
            }

            ScreenStates.BIN_EDIT -> {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    var binName by rememberSaveable { mutableStateOf("") }
                    var binDescription by rememberSaveable { mutableStateOf("") }
                    Text(
                        text = "Edit Bin",
                        modifier = Modifier.padding(top = 150.dp, bottom = 4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NameField(name = binName, onNameChange = { binName = it})
                    Spacer(modifier = Modifier.height(16.dp))
                    DescriptionField(
                        description = binDescription,
                        onDescriptionChange = { binDescription = it}
                    )
                }
            }
        }
    }
}

@Composable
fun NameField(name: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Enter Bin Name") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun DescriptionField(description: String, onDescriptionChange: (String) -> Unit) {
    OutlinedTextField(
        value = description,
        onValueChange = onDescriptionChange,
        label = { Text("Enter Bin Description") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}