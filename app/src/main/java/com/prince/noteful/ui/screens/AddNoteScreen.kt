package com.prince.noteful.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.prince.noteful.data.local.NoteEntity
import com.prince.noteful.ui.viewModels.NotefulViewModel
import java.util.UUID

@Composable
fun AddNoteScreen(
    viewModel: NotefulViewModel,
    onDismiss: ()-> Unit
) {
    var titleInput by remember { mutableStateOf("") }
    var contentInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Add Note",
                style = MaterialTheme.typography.headlineLarge
            )
            Surface(
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                IconButton(
                    onClick = {
                        if (titleInput.isNotBlank() || contentInput.isNotBlank()) {
                            viewModel.saveNote(
                                NoteEntity(
                                    id = UUID.randomUUID().toString(),
                                    title = titleInput.trim(),
                                    content = contentInput.trim()
                                )
                            )
                            titleInput = ""
                            contentInput = ""
                        }
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

        }

        Spacer(Modifier.height(16.dp))

        TextField(
            value = titleInput,
            onValueChange = { titleInput=it },
            placeholder = { Text("Add Title") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = contentInput,
            onValueChange = { contentInput=it },
            placeholder = { Text("Write your note") },
            singleLine = false,
            maxLines = Int.MAX_VALUE,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState),
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (titleInput.isNotBlank() || contentInput.isNotBlank() ){
                    viewModel.saveNote(
                        NoteEntity(
                            id = UUID.randomUUID().toString(),
                            title = titleInput.trim(),
                            content = contentInput.trim()
                        )
                    )
                    titleInput = ""
                    contentInput = ""
                    onDismiss()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            enabled = titleInput.isNotBlank() || contentInput.isNotBlank()
        ) {
            Text("Save Note")
        }
    }
}