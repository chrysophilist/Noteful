package com.prince.noteful.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.NotificationAdd
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DynamicDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDelete: (() -> Unit) ?= null,
    onShare: (() -> Unit) ?= null,
    onPin: (() -> Unit) ?= null,
    onReminder: (() -> Unit) ?= null,
    onArchive: (() -> Unit) ?= null
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier
            .width(160.dp),
        shape = MaterialTheme.shapes.large
    ) {
        onPin?.let {
            DropdownMenuItem(
                onClick = { onPin() },
                text = {Text("Pin")},
                leadingIcon = {Icon(Icons.Default.PushPin, "Pin")}
            )
        }
        onReminder?.let {
            DropdownMenuItem(
                onClick = { onReminder() },
                text = {Text("Reminder")},
                leadingIcon = {Icon(Icons.Default.NotificationAdd, "Reminder")}
            )
        }
        onArchive?.let {
            DropdownMenuItem(
                onClick = { onArchive() },
                text = {Text("Archive")},
                leadingIcon = {Icon(Icons.Default.Archive, "Archive")}
            )
        }
        onDelete?.let {
            DropdownMenuItem(
                onClick = { onDelete() },
                text = {Text("Delete")},
                leadingIcon = {Icon(Icons.Default.DeleteForever, "Delete")}
            )
        }
        onShare?.let {
            HorizontalDivider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.background)
            DropdownMenuItem(
                onClick = { onShare() },
                text = {Text("Share")},
                leadingIcon = {Icon(Icons.Default.Share, "Share")}
            )
        }
    }
}