package com.emilioaugust.copypus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MultiPasteSheet(onDismiss: () -> Unit, onSaveAll: (List<String>) -> Unit) {
    var currentText by rememberSaveable { mutableStateOf("") }
    val items = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        Text(
            text = "Multi Paste",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = currentText,
            onValueChange = {
                currentText = it
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            placeholder = {
                Text("Paste text here")
            }
        )

        Spacer(Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = currentText.isNotBlank(),
            onClick = {
                items.add(currentText.trim())
                currentText = ""
            }
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(text = "Add", color = Color.White)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Added: ${items.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(items) { index, item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = item,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                items.removeAt(index)
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = items.isNotEmpty(),
            onClick = {
                onSaveAll(items.toList())
                onDismiss()
            }
        ) {
            Icon(Icons.Default.Save, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Save All", color = Color.White)
        }
    }
}