package com.example.resourceful.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.resourceful.domain.model.ResourceEntity
import com.example.resourceful.presentation.HomeViewModel
import com.example.resourceful.util.AppColors
import kotlinx.coroutines.CoroutineScope

@Composable
fun ResourceItem(
    resource: ResourceEntity,
    viewModel: HomeViewModel,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topEnd = 30.dp, bottomStart = 30.dp))
            .padding(4.dp),
        color = AppColors.resourceRow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
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
                onClick = {
                    // Todo Add edit functionality
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    modifier = Modifier.size(28.dp),
                    contentDescription = "Edit Resource Icon"
                )
            }
        }
    }
}