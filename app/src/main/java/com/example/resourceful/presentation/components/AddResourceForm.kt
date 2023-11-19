package com.example.resourceful.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.resourceful.presentation.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddResourceForm(
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope,
    parentFolder: Int = 0
) {
    val focusManager = LocalFocusManager.current

    var title by rememberSaveable { mutableStateOf("") }
    var link by rememberSaveable { mutableStateOf("") }

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
                        parent = parentFolder
                    )
                }
            }
        ) {
            Text(text = "Save")
        }
    }
}