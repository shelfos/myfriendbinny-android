package com.example.myfriendbinny.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfriendbinny.AppHeader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppScaffold(
    showPlus: Boolean = false,
    onPlusClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth().padding(vertical = 48.dp)
            ) {
                AppHeader()
            }
        },
        floatingActionButton = {
            if (showPlus && onPlusClick != null) {
                FloatingActionButton(onClick = onPlusClick) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) {
        content()
    }
}