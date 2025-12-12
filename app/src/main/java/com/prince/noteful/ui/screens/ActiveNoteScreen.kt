package com.prince.noteful.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.prince.noteful.data.local.NoteEntity
import com.prince.noteful.ui.viewModels.NotefulViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveNoteScreen(
    viewModel: NotefulViewModel,
    onBack: ()-> Unit
) {
    val activeNote by viewModel.activeNote.collectAsState()

    var titleInput by rememberSaveable { mutableStateOf( "") }
    var contentInput by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(activeNote) {
        if (activeNote != null){
            titleInput = activeNote?.title.orEmpty()
            contentInput = activeNote?.content.orEmpty()
        }
    }

    fun onSave(){
        if (titleInput.isNotBlank() || contentInput.isNotBlank()){
            val note = NoteEntity(
                id = activeNote?.id ?: UUID.randomUUID().toString(),
                title = titleInput.trim(),
                content = contentInput.trim()
            )
            viewModel.saveNote(note)
        } else {
            activeNote?.let { thisNote->
                viewModel.deleteNote(thisNote)
            }
            onBack()
        }
    }

    val hasModified = (titleInput != activeNote?.title.orEmpty()) || (contentInput != activeNote?.content.orEmpty())

    var showDialog by remember { mutableStateOf(false) }
    BackHandler(
        enabled = hasModified
    ) {
        showDialog = true
    }

    if (showDialog){
        BasicAlertDialog(
            onDismissRequest = { showDialog=false }
        ){
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
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
                                onBack()
                            }
                        ) {
                            Text("Discard")
                        }
                        TextButton(
                            onClick = {
                                showDialog = false
                                onSave()
                            }
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onSave()
                            onBack()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Return to home screen"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.PushPin, "Pin") }
                    IconButton(onClick = {}) { Icon(Icons.Default.NotificationAdd, "Reminder") }
                    IconButton(onClick = {}) { Icon(Icons.Default.Archive, "Archive") }
                    Box {
                        var showMenu by remember { mutableStateOf(false) }
                        IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, "More") }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    activeNote?.let { thisNote->
                                        viewModel.deleteNote(thisNote)
                                    }
                                    showMenu = false
                                    onBack()
                                },
                                text = {Text("Delete")},
                                leadingIcon = {Icon(Icons.Default.DeleteForever, "Delete")}
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding-> 
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ){
            TextField(
                value = titleInput,
                onValueChange = { titleInput=it },
                placeholder = {Text("Add Title")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(4.dp))

            TextField(
                value = contentInput,
                onValueChange = { contentInput=it },
                placeholder = {Text("Start writing...")},
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(8.dp))

            if (hasModified){
                Button(
                    onClick = {
                        onSave()
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                ) {
                    Text("Save Note")
                }
            }
        }
    }
}