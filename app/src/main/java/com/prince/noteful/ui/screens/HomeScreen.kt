package com.prince.noteful.ui.screens

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prince.noteful.ui.viewModels.NotefulViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NotefulViewModel,
    onCardClick: ()-> Unit
) {
    var isNoteSheetOpen by rememberSaveable() { mutableStateOf(false) }

    if(isNoteSheetOpen){
        ModalBottomSheet(
            onDismissRequest = { isNoteSheetOpen = false },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            )
        ) {
            AddNoteScreen(
                viewModel,
                onDismiss = { isNoteSheetOpen=false }
            )
        }

    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Noteful") },
                navigationIcon = {
                    IconButton( onClick = {} ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, "Search") }
                    IconButton(onClick = {}) { Icon(Icons.Default.AccountCircle, "Account") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isNoteSheetOpen = true }
            ) {
                Icon(Icons.Default.Add, "Add Note")
            }
        }
    ) { innerPadding->

        val notes by viewModel.notes.collectAsState(emptyList())

        if (notes.isEmpty()){
            Box(
                modifier=Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment= Alignment.Center
            ){
                Text(
                    "Notes you add appear here",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notes){ note->
                    NoteCard(
                        title = note.title,
                        content = note.content,
                        onClick = {
                            viewModel.loadNote(note.id)
                            onCardClick()
                        },
                        onDelete = { viewModel.deleteNote(note) }
                    )
                }
            }
        }
    }
}

@Composable
fun NoteCard(
    title: String,
    content: String,
    onClick: ()-> Unit,
    onDelete: ()-> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
                .combinedClickable(
                enabled = true,
                onClick = { onClick() },
                onLongClick = { showMenu = true }
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onDelete()
                    showMenu = false
                },
                text = {Text("Delete")},
                leadingIcon = {Icon(Icons.Default.DeleteForever, "Delete")}
            )
        }
    }
}
