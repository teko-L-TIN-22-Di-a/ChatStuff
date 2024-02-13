package com.example.chatapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingRow(viewModel: ChatViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(
                text = "Dark Mode",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium)
        }
        Column {
            Switch(checked = uiState.darkMode, onCheckedChange = {
                viewModel.setDarkMode(it)
            })
        }

        Text(uiState.darkMode.toString())
    }
}

@Composable
fun SettingsScreen() {
    SettingRow()
}


@Preview
@Composable
fun SettingsPreview() {
    SettingsScreen()
}