package com.example.resourceful.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.resourceful.R
import com.example.resourceful.presentation.components.AddResourceForm
import com.example.resourceful.presentation.components.FolderItem
import com.example.resourceful.presentation.components.ResourceItem
import com.example.resourceful.util.AppColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var name by rememberSaveable { mutableStateOf("") }

    var title by remember { mutableStateOf("") }
    var link by remember { mutableStateOf("") }

    var isAddingFolder by rememberSaveable { mutableStateOf(false) }
    var isAddingResource by rememberSaveable { mutableStateOf(false) }

    val folders = viewModel.getFolders(0).collectAsState().value
    val resources = viewModel.getResources(0).collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "ResourceFul",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppColors.titleBar)
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) { topBarPadding ->
        Column(
            modifier = Modifier.padding(top = topBarPadding.calculateTopPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Reservoir",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            isAddingFolder = !isAddingFolder
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
                            isAddingResource = !isAddingResource
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LibraryAdd,
                            modifier = Modifier.size(26.dp),
                            contentDescription = "Add Resource icon"
                        )
                    }
                }

            }

            AnimatedVisibility(visible = isAddingFolder) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text(text = "New Folder") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp, start = 18.dp, end = 18.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                isAddingFolder = false
                                coroutineScope.launch {
                                    viewModel.addFolder(name, 0)
                                }
                            }
                        )
                    )
                }
            }
            AnimatedVisibility(visible = isAddingResource) {
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
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
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
                                    parent = 0
                                )
                            }
                            isAddingResource = false
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            }

            folders.forEach { folder ->
                FolderItem(folder, viewModel, coroutineScope)
            }
            resources.forEach { resource ->
                ResourceItem(resource, viewModel, coroutineScope)
            }
        }
    }
}