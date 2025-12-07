package com.prince.noteful.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    onSave: ()-> Unit
) {
    val activeNote by viewModel.activeNote.collectAsState()
    val currentNoteId = rememberSaveable(activeNote?.id) {
        activeNote?.id ?: UUID.randomUUID().toString()
    }

    var titleInput by rememberSaveable { mutableStateOf( "") }
    var contentInput by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(activeNote) {
        if (activeNote != null){
            titleInput = activeNote?.title.orEmpty()
            contentInput = activeNote?.content.orEmpty()
        }
    }

    val hasModified = (titleInput != activeNote?.title.orEmpty()) || (contentInput != activeNote?.content.orEmpty())

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
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
                            }
                            onSave()
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
                    IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, "More") }
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
                            onSave()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                ) {
                    Text("Save Note")
                }
            }
        }
    }
}