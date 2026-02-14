package com.prince.noteful.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            .imePadding()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "New Note",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            FilledTonalIconButton(
                onClick = {
                    onDismiss()
                    onSave()
                },
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }

        }

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(22.dp))
        ) {
            TextField(
                value = titleInput,
                onValueChange = { titleInput=it },
                placeholder = {
                    Text(
                        text = "Add Title",
                        style = MaterialTheme.typography.titleLarge
                    ) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            HorizontalDivider(thickness = 3.dp, color = MaterialTheme.colorScheme.surfaceContainer)

            TextField(
                value = contentInput,
                onValueChange = { contentInput=it },
                placeholder = { Text("Start typing...") },
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        if (
            titleInput.isNotBlank() || contentInput.isNotBlank()
        ){
            Button(
                onClick = {
                    onDismiss()
                    onSave()
                },
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Text(
                    text = "Save Note",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}