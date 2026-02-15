package com.prince.noteful.ui.screens

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prince.noteful.data.local.NoteEntity
import com.prince.noteful.navigation.AppScaffoldState
import com.prince.noteful.ui.components.DynamicDropdownMenu
import com.prince.noteful.ui.viewModels.NotefulViewModel
import com.prince.noteful.ui.viewModels.PrefViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    setScaffoldState: (AppScaffoldState) -> Unit,
    innerPadding: PaddingValues,
    viewModel: NotefulViewModel,
    onCardClick: ()-> Unit,
    prefviewModel: PrefViewModel
) {
    var isNoteSheetOpen by rememberSaveable() { mutableStateOf(false) }
    val isGrid by prefviewModel.isGridView.collectAsState()

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val query = textFieldState.text.toString()

    val notes by viewModel.notes.collectAsState(emptyList())

    val searchedNotes = if(query.isEmpty()){
        notes
    } else {
        notes.filter { note ->
            note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
        }
    }

    if(isNoteSheetOpen){
        ModalBottomSheet(
            onDismissRequest = { isNoteSheetOpen = false },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true,
            ),
            modifier = Modifier.statusBarsPadding(),
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            AddNoteScreen(
                viewModel,
                onDismiss = { isNoteSheetOpen=false }
            )
        }

    }

    SideEffect {
        setScaffoldState(
            AppScaffoldState(
                modifier = Modifier
                    .fillMaxSize(),
                topBar = {
                    AppBarWithSearch(
                        state = searchBarState,
                        navigationIcon = {
                            IconButton( onClick = {} ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) { Icon(Icons.Default.AccountCircle, "Account") }
                        },
                        inputField = {
                            HomeScreenInputField(
                                textFieldState = textFieldState,
                                searchBarState = searchBarState,
                                isGrid = isGrid,
                                onToggleGrid = { prefviewModel.setGridView() }
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { isNoteSheetOpen = true },
                        shape = MaterialTheme.shapes.extraLarge
                    ){
                        Icon(Icons.Default.Add, "Add Note")
                    }
                }
            )
        )
    }

    if (searchBarState.currentValue == SearchBarValue.Expanded){
        ExpandedFullScreenSearchBar(
            state = searchBarState,
            inputField = {
                HomeScreenInputField(
                    textFieldState = textFieldState,
                    searchBarState = searchBarState,
                    isGrid = isGrid,
                    onToggleGrid = { prefviewModel.setGridView() }
                )
            }
        ) {
            if (searchedNotes.isEmpty() && query.isNotBlank()) {
                Box(
                    modifier=Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment= Alignment.Center
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Search, null)
                        Text( text = "No notes found" )
                    }
                }
            } else {
                NotesListContent(
                    isGrid = isGrid,
                    innerPadding = innerPadding,
                    searchedNotes = searchedNotes,
                    viewModel = viewModel,
                    onCardClick = onCardClick
                )
            }
        }
    } else {
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NoteAlt,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp)
                    )
                    Text(
                        "Notes you add appear here",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            NotesListContent(
                isGrid = isGrid,
                innerPadding = innerPadding,
                searchedNotes = searchedNotes,
                viewModel = viewModel,
                onCardClick = onCardClick
            )
        }
    }
}

@Composable
fun NotesListContent(
    isGrid: Boolean,
    innerPadding: PaddingValues,
    searchedNotes: List<NoteEntity>,
    viewModel: NotefulViewModel,
    onCardClick: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = if (isGrid) {
            StaggeredGridCells.Adaptive(160.dp)
        } else {
            StaggeredGridCells.Fixed(1)
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp
    ) {
        items(searchedNotes) { note ->
            NoteCard(
                title = note.title,
                content = note.content,
                onClick = {
                    viewModel.loadNote(note.id)
                    onCardClick()
                },
                onDelete = { viewModel.deleteNote(note) },
                isGridOn = isGrid
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenInputField(
    textFieldState: TextFieldState,
    searchBarState: SearchBarState,
    isGrid: Boolean,
    onToggleGrid: () -> Unit
) {
    val scope = rememberCoroutineScope()

    SearchBarDefaults.InputField(
        textFieldState = textFieldState,
        searchBarState = searchBarState,
        onSearch = {},
        placeholder = {
            if (searchBarState.currentValue == SearchBarValue.Collapsed){
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clearAndSetSemantics {},
                    text = "Noteful",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        leadingIcon = {
            if (searchBarState.currentValue == SearchBarValue.Expanded){
                IconButton(
                    onClick = {
                        scope.launch {
                            searchBarState.animateToCollapsed()
                        }
                        textFieldState.clearText()
                    }
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Close Search")}
            }
        },
        trailingIcon = {
            Row {
                IconButton(
                    onClick = {
                        onToggleGrid()
                    }
                ) {
                    Icon(
                        if (isGrid) Icons.Default.ViewAgenda else Icons.Default.GridView,
                        if (isGrid) "List view" else "Grid view"
                    )
                }
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Default.SwapVert,
                        "Sort"
                    )
                }
                if (searchBarState.currentValue == SearchBarValue.Expanded && textFieldState.text.isNotBlank()){
                    IconButton(
                        onClick = {
                            textFieldState.clearText()
                        }
                    ) {
                        Icon(Icons.Default.Clear, "Clear Search")
                    }
                }
            }
        }
    )
}
@Composable
fun NoteCard(
    title: String,
    content: String,
    onClick: ()-> Unit,
    onDelete: ()-> Unit,
    isGridOn: Boolean
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.clip(MaterialTheme.shapes.large)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    enabled = true,
                    onClick = { onClick() },
                    onLongClick = { showMenu = true }
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            if (isGridOn) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    if (title.isNotEmpty()){
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    if (content.isNotEmpty()){
                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 10,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp)
                    ) {
                        if(title.isNotEmpty()){
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        if(content.isNotEmpty()){
                            Text(
                                text = content,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 10,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(48.dp)
                    ) {
                        IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, "More") }
                        DynamicDropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            onDelete = {
                                showMenu = false
                                onDelete()
                            },
                            onPin = {},
                            onReminder = {},
                            onArchive = {},
                            onShare = {}
                        )
                    }
                }
            }
        }

        if (isGridOn){
            DynamicDropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                onDelete = {
                    showMenu = false
                    onDelete()
                },
                onPin = {},
                onReminder = {},
                onArchive = {},
                onShare = {}
            )
        }
    }
}

