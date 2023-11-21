package com.example.resourceful.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.resourceful.domain.model.ResourceEntity
import com.example.resourceful.presentation.HomeViewModel
import com.example.resourceful.util.AlertMessages
import com.example.resourceful.util.AppColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ResourceItem(
    resource: ResourceEntity,
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isEditingResource by rememberSaveable { mutableStateOf(false) }
    var isDeletionDialogVisible by rememberSaveable { mutableStateOf(false) }

    var title by rememberSaveable { mutableStateOf(resource.title) }
    var link by rememberSaveable { mutableStateOf(resource.link) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 30.dp, bottomStart = 30.dp)),
            color = AppColors.resourceRow
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 5.dp),
                    contentDescription = "Bullet Point icon"
                )
                Text(
                    text = resource.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(resource.link)
                        )
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Link,
                        modifier = Modifier.size(28.dp),
                        contentDescription = "Open Link icon"
                    )
                }
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        isEditingResource = !isEditingResource
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        modifier = Modifier.size(22.dp),
                        contentDescription = "Edit Resource Icon"
                    )
                }
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        isDeletionDialogVisible = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        modifier = Modifier.size(22.dp),
                        contentDescription = "Delete icon"
                    )
                }
            }
        }

        if(isDeletionDialogVisible) {
            DeletionDialog(
                description = AlertMessages.deleteFolderText,
                onDismiss = { isDeletionDialogVisible = false }) {
                coroutineScope.launch {
                    viewModel.deleteResource(resource)
                }
            }

        }


        AnimatedVisibility(visible = isEditingResource) {
            Column {
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
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                )
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        val updatedResource = resource.copy(title = title, link = link)
                        coroutineScope.launch {
                            viewModel.updateResource(updatedResource)
                        }
                        isEditingResource = false
                    }
                ) {
                    Text(text = "Save Changes")
                }
            }

        }
    }
}