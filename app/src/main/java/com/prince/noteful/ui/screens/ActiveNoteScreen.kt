package com.prince.noteful.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prince.noteful.data.local.NoteEntity
import com.prince.noteful.navigation.AppScaffoldState
import com.prince.noteful.ui.components.DynamicDropdownMenu
import com.prince.noteful.ui.viewModels.NotefulViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActiveNoteScreen(
    setScaffoldState: (AppScaffoldState) -> Unit,
    innerPadding: PaddingValues,
    viewModel: NotefulViewModel,
    onBack: ()-> Unit
) {
    val activeNote by viewModel.activeNote.collectAsState()

    var titleInput by rememberSaveable { mutableStateOf( "") }
    var contentInput by rememberSaveable { mutableStateOf("") }
    val hasModified = (titleInput != activeNote?.title.orEmpty()) || (contentInput != activeNote?.content.orEmpty())

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
        }
    }

    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val isCollapsed by remember {
        derivedStateOf {
            scrollBehavior.state.collapsedFraction > 0.5f
        }
    }

    BackHandler(
        enabled = true
    ) {
        onBack()
        onSave()
    }

    SideEffect {
        setScaffoldState(
            AppScaffoldState(
                modifier = Modifier
                    .fillMaxSize().imePadding().nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    MediumTopAppBar(
                        title = {
                            TextField(
                                value = titleInput,
                                onValueChange = { titleInput=it },
                                placeholder = {Text("Add Title")},
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
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
                                    onBack()
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
                            if (!isCollapsed){
                                IconButton(onClick = {}) { Icon(Icons.Default.PushPin, "Pin") }
                                IconButton(onClick = {}) { Icon(Icons.Default.NotificationAdd, "Reminder") }
                                IconButton(onClick = {}) { Icon(Icons.Default.Archive, "Archive") }
                            }
                            Box {
                                var showMenu by remember { mutableStateOf(false) }
                                IconButton(onClick = { showMenu = true }) { Icon(Icons.Default.MoreVert, "More") }
                                if (isCollapsed){
                                    DynamicDropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false },
                                        onDelete = {
                                            onBack()
                                            activeNote?.let { thisNote->
                                                viewModel.deleteNote(thisNote)
                                            }
                                            showMenu = false
                                        },
                                        onPin = {},
                                        onReminder = {},
                                        onArchive = {},
                                        onShare = {}
                                    )
                                } else {
                                    DynamicDropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false },
                                        onDelete = {
                                            onBack()
                                            activeNote?.let { thisNote->
                                                viewModel.deleteNote(thisNote)
                                            }
                                            showMenu = false
                                        },
                                        onShare = {}
                                    )
                                }
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },

                bottomBar = {
                    if (hasModified){
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Button(
                                onClick = {
                                    onSave()
                                },
                                shape = CircleShape,
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .height(56.dp)
                            ) {
                                Text(
                                    text = "Save Note",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            )
        )
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top
    ){

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
            )
        )
    }
}