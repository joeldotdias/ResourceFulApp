package com.example.resourceful.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.resourceful.R
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.presentation.components.FolderItem
import com.example.resourceful.util.AppColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val name by remember { mutableStateOf("") }

    val title by remember { mutableStateOf("") }
    val link by remember { mutableStateOf("") }

    val folders = viewModel.getFolders(0).collectAsState().value

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
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your folders",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )
                IconButton(
                    onClick = {
                        //
                    }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.add_folder_icon),
                        modifier = Modifier.size(32.dp),
                        contentDescription = "Add Folder icon"
                    )
                }
            }

            folders.forEach { folder ->
                FolderItem(folder, viewModel, coroutineScope)
            }
        }
    }
}



@Composable
fun HomeScree(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val fl = viewModel.getFolders(11).collectAsState().value
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.addFolder(name = "Tempo", parent = 0)
                    }
                }) {

            }
            fl.forEach { folder ->
                ProvText(viewModel, folder, coroutineScope)
            }



        }
    }
}

@Composable
fun ProvText(
    viewModel: HomeViewModel,
    folder: FolderEntity,
    coroutineScope: CoroutineScope
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    coroutineScope.launch { viewModel.deleteFolder(folder) }

                }
        ) {
            Text(text = folder.name)
            Column(
                modifier = Modifier.padding(start = 15.dp)
            ) {
                val nef = viewModel.getFolders(folder.id).collectAsState().value
                nef.forEach { ne ->
                    ProvText(viewModel, ne, coroutineScope)
                }
            }
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.addFolder(name = "Tempo", parent = folder.id)
                }
            }) {

        }
    }


}