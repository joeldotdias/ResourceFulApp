package com.example.resourceful.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resourceful.R
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.presentation.HomeViewModel
import com.example.resourceful.util.AlertMessages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FolderItem(
    folder: FolderEntity,
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
) {
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current

    var isFolderExpanded by rememberSaveable { mutableStateOf(false) }
    var isAddFolderFormVisible by rememberSaveable { mutableStateOf(false) }
    var isAddResourceFormVisible by rememberSaveable { mutableStateOf(false) }
    var isContextMenuVisible by rememberSaveable { mutableStateOf(false) }
    var isEditingFolder by rememberSaveable { mutableStateOf(false) }
    var isDeletionDialogVisible by rememberSaveable { mutableStateOf(false) }

    var itemHeight by remember { mutableStateOf(0.dp) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    val dropdownItems = listOf("Edit", "Delete")

    var name by remember { mutableStateOf("") }

    var title by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    val folders = viewModel.getFolders(folder.id).collectAsState().value
    val resources = viewModel.getResources(folder.id).collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .pointerInput(true) {
                    detectTapGestures(
                        onLongPress = {
                            isContextMenuVisible = true
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                        }
                    )
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(end = 7.dp),
                onClick = {
                    if(isFolderExpanded) {
                        isAddFolderFormVisible = false
                        isAddResourceFormVisible = false
                    }
                    isFolderExpanded = !isFolderExpanded
                }
            ) {
                Image(
                    painter = painterResource(id = if (isFolderExpanded) R.drawable.open_folder_icon else R.drawable.closed_folder_icon),
                    modifier = Modifier.size(34.dp),
                    contentDescription = "Opened/Closed Folder icon"
                )
            }
            Text(
                text = folder.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )
            
            DropdownMenu(
                expanded = isContextMenuVisible,
                onDismissRequest = { isContextMenuVisible = false }
            ) {
                dropdownItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        leadingIcon = {
                            Icon(imageVector = if(item == "Delete") Icons.Filled.Delete else Icons.Filled.Edit, contentDescription = "")
                        },
                        onClick = {
                            if(item == "Delete") {
                                 isDeletionDialogVisible = true
                            }
                            else {
                                isEditingFolder = true
                            }
                            isContextMenuVisible = false
                        }
                    )
                }
            }

            if(isFolderExpanded) {
                IconButton(
                    onClick = {
                        isAddFolderFormVisible = !isAddFolderFormVisible
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.add_folder_icon),
                        modifier = Modifier.size(32.dp),
                        contentDescription = "Add Folder icon"
                    )
                }
                IconButton(
                    onClick = {
                        isAddResourceFormVisible = !isAddResourceFormVisible
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.LibraryAdd,
                        contentDescription = "Add Resource icon"
                    )
                }
            }
        }

        if(isDeletionDialogVisible) {
            DeletionDialog(
                description = AlertMessages.deleteFolderText,
                onDismiss = { isDeletionDialogVisible = false }
            ) {
                coroutineScope.launch {
                    viewModel.deleteFolder(folder)
                }
            }
        }

        AnimatedVisibility(visible = ((isFolderExpanded && isAddFolderFormVisible)) || isEditingFolder) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(isEditingFolder) name = folder.name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { if(!isEditingFolder) Text(text = "New Folder") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, bottom = 5.dp),
                    trailingIcon = {
                        if(isEditingFolder) {
                            IconButton(onClick = {
                                isEditingFolder = false
                            }) { Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null) }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            isAddFolderFormVisible = false
                            if(isEditingFolder) {
                                val updatedFolder = folder.copy(name = name)
                                coroutineScope.launch { viewModel.updateFolder(updatedFolder) }
                                isEditingFolder = false
                            }
                            else {
                                coroutineScope.launch { viewModel.addFolder(name, folder.id) }
                            }
                        }
                    )
                )
            }
        }

        AnimatedVisibility(visible = (isFolderExpanded && isAddResourceFormVisible)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(text = "Title") },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext ={
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )
                OutlinedTextField(
                    value = link,
                    onValueChange = { link = it },
                    label = { Text(text = "Link") },
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext ={
                            focusManager.clearFocus()
                        }
                    )
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        coroutineScope.launch {
                            viewModel.addResource(
                                title = title,
                                link = link,
                                parent = folder.id
                            )
                        }
                        isAddResourceFormVisible = false
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
        
        AnimatedVisibility(visible = isFolderExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                folders.forEach { folder ->
                    FolderItem(folder, viewModel, coroutineScope)
                }
                resources.forEach { resource ->
                    ResourceItem(resource, viewModel, coroutineScope)
                }
            }
        }
    }
}