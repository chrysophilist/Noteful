package com.prince.noteful

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(viewModel: MyViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var isDialogOpen by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(left = 5.dp, right = 0.dp, top = 10.dp, bottom = 0.dp),
                title = {
                    Text(text = "Noteful", fontSize = 25.sp)
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.padding(end = 5.dp),
                    )
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        modifier = Modifier.padding(end = 5.dp)
                    )
                },

                )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isDialogOpen = true }
            ) {}
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add"
            )
        },
    ) {
        if (isDialogOpen) {
            AddDialog(
                text = state.value.note,
                onTextChange = { viewModel.updateNote(it) },
                onSaveClick = {
                    viewModel.addNote()
                    isDialogOpen = false
                }
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
        {
            items(state.value.notesList) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Box(
                        Modifier.fillMaxSize()
                    ){
                        //Notes
                        Text(text = note, modifier = Modifier.padding(12.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Search",
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .align(Alignment.CenterEnd)
                                .clickable(
                                    onClick = { viewModel.removeNote() }
                                )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddDialog(
    modifier: Modifier = Modifier,
    text: String = "",
    onTextChange: (String) -> Unit = {},
    onSaveClick: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )
                Text(
                    text = "Add Note",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),

                    )
                Spacer(modifier = Modifier.height(12.dp))
                TextFieldState(
                    textFieldValue = text,
                    placeholder = "Enter Your Note",
                    onValueChange = onTextChange,
//                    modifier = Modifier.size(width = 300.dp, height = 100.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { onSaveClick() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}


@Composable
fun TextFieldState(
    textFieldValue: String,
    onValueChange: (String) -> Unit = {},
    placeholder: String,
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            onValueChange(it)
        },
        placeholder = {
            Text(text = "Write your note here")
        },
        modifier = Modifier.size(width = 300.dp, height = 100.dp)
    )
}