package com.example.resourceful.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.resourceful.domain.model.FolderEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
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
                        viewModel.addFolder(FolderEntity(name = "Tempo", parent = 0))
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
                    viewModel.addFolder(FolderEntity(name = "Tempo", parent = folder.id))
                }
            }) {

        }
    }


}