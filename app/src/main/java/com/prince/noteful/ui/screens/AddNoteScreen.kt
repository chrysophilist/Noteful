package com.prince.noteful.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prince.noteful.data.local.NoteEntity
import com.prince.noteful.ui.viewModels.NotefulViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NotefulViewModel,
    onDismiss: ()-> Unit
) {

    var titleInput by rememberSaveable { mutableStateOf("") }
    var contentInput by rememberSaveable { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    BackHandler(
        enabled = true
    ) {
        if (titleInput.isNotBlank() || contentInput.isNotBlank() ){
            showDialog = true
        } else {
            onDismiss()
        }
    }

    fun onSave(){
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
        }
    }

    if (showDialog){
        BasicAlertDialog(
            onDismissRequest = { showDialog=false }
        ){
            Surface(
                modifier = Modifier.fillMaxWidth(0.9f).wrapContentHeight(),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = AlertDialogDefaults.TonalElevation,
                color = AlertDialogDefaults.containerColor
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Unsaved Changes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier=Modifier.height(16.dp))
                    Text(
                        text = "You have unsaved edits. Do you want to save before leaving?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier=Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                showDialog = false
                                onDismiss()
                            }
                        ) {
                            Text("Discard")
                        }
                        TextButton(
                            onClick = {
                                showDialog = false
                                onSave()
                                onDismiss()
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

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
                modifier = Modifier
                    .size(48.dp),
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                IconButton(
                    onClick = {
                        onDismiss()
                        onSave()
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
                .weight(1f),
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
                onDismiss()
                onSave()
            },
            enabled = titleInput.isNotBlank() || contentInput.isNotBlank(),
            shape = CircleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Save Note",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
    }
}