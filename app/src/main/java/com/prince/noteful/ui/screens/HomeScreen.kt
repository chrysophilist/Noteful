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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.prince.noteful.ui.viewModels.NotefulViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NotefulViewModel,
    onCardClick: ()-> Unit
) {
    var isNoteSheetOpen by rememberSaveable() { mutableStateOf(false) }
    var isGrid by rememberSaveable { mutableStateOf(true) }

    var isSearchBarActive by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

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
            if (isSearchBarActive){
                TopAppBar(
                    title = {
                        TextField(
                            value = query,
                            onValueChange = {
                                query=it
                            },
                            placeholder = {Text("Search notes...")},
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                isSearchBarActive = false
                                query = ""
                            }
                        ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Close Search")}
                    },
                    actions = {
                        if (query.isNotBlank()){
                            IconButton(
                                onClick = { query = "" }
                            ) {
                                Icon(Icons.Default.Clear, "Clear Search")
                            }
                        }
                    }
                )
            } else {
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
                        IconButton(onClick = { isSearchBarActive=true }) { Icon(Icons.Default.Search, "Search") }
                        if (isGrid){
                            IconButton(onClick = { isGrid = false}) { Icon(Icons.Default.ViewAgenda, "Single-column view") }
                        } else {
                            IconButton(onClick = { isGrid = true }) { Icon(Icons.Default.GridView, "Multi-column view") }
                        }
                        IconButton(onClick = {}) { Icon(Icons.Default.AccountCircle, "Account") }
                    }
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { isNoteSheetOpen = true },
                icon = { Icon(Icons.Default.Add, "Add Note") },
                text = {Text("Add Note")},
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 20.dp)
            )
        }
    ) { innerPadding->

        val notes by viewModel.notes.collectAsState(emptyList())

        val searchedNotes = if(query.isEmpty()){
            notes
        } else {
            notes.filter { note ->
                note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
            }
        }

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
                columns = if (isGrid){
                    GridCells.Adaptive(160.dp)
                } else {
                    GridCells.Fixed(1)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchedNotes){ note->
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
            modifier = Modifier
                .fillMaxWidth()
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
