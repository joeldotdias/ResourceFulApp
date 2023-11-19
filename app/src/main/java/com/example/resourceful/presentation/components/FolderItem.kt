package com.example.resourceful.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resourceful.R
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.presentation.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun FolderItem(
    folder: FolderEntity,
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
) {
    val focusManager = LocalFocusManager.current

    var isFolderExpanded by rememberSaveable { mutableStateOf(false) }
    var showAddFolderForm by rememberSaveable { mutableStateOf(false) }
    var showAddResourceForm by rememberSaveable { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }

    var title by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    val folders = viewModel.getFolders(folder.id).collectAsState().value
    val resources = viewModel.getResources(folder.id).collectAsState().value

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.padding(end = 7.dp),
                onClick = {
                    if(isFolderExpanded) {
                        showAddFolderForm = false
                        showAddResourceForm = false
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

            if(isFolderExpanded) {
                IconButton(
                    onClick = {
                        showAddFolderForm = !showAddFolderForm
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
                        showAddResourceForm = !showAddResourceForm
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.LibraryAdd,
                        contentDescription = "Add Resource icon"
                    )
                }
            }
        }

        AnimatedVisibility(visible = (isFolderExpanded && showAddFolderForm)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text(text = "New Folder") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            showAddFolderForm = false
                            coroutineScope.launch {
                                viewModel.addFolder(name, folder.id)
                            }
                        }
                    )
                )
            }
        }

        AnimatedVisibility(visible = (isFolderExpanded && showAddResourceForm)) {
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
                    onClick = {
                        coroutineScope.launch {
                            viewModel.addResource(
                                title = title,
                                link = link,
                                parent = folder.id
                            )
                        }
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