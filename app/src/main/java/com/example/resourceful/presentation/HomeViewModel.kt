package com.example.resourceful.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.domain.model.ResourceEntity
import com.example.resourceful.domain.repository.ResourceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ResourceRepository
): ViewModel() {

    fun getFolders(folderParent: Int): StateFlow<List<FolderEntity>> {
        val folders = MutableStateFlow<List<FolderEntity>>(emptyList())

        viewModelScope.launch(Dispatchers.IO) {
            repository.getFolders(folderParent)
                .distinctUntilChanged()
                .collect { listOfFolders ->
                    folders.value = listOfFolders
                }
        }
        return folders.asStateFlow()
    }

    fun getResources(resourceParent: Int): StateFlow<List<ResourceEntity>> {
        val resources = MutableStateFlow<List<ResourceEntity>>(emptyList())

        viewModelScope.launch(Dispatchers.IO) {
            repository.getResources(resourceParent)
                .distinctUntilChanged()
                .collect { listOfResources ->
                    resources.value = listOfResources
                }
        }
        return resources.asStateFlow()
    }

    suspend fun addFolder(name: String, parent: Int) {
        val folderEntity = FolderEntity(name = name, parent = parent)
        repository.upsertFolder(folderEntity)
    }

    suspend fun addResource(title: String, link: String, parent: Int) {
        val resourceEntity = ResourceEntity(title = title, link = link, parent = parent)
        repository.upsertResource(resourceEntity)
    }

    suspend fun updateFolder(folderEntity: FolderEntity) {
        repository.upsertFolder(folderEntity)
    }

    suspend fun updateResource(resourceEntity: ResourceEntity) {
        repository.upsertResource(resourceEntity)
    }

    suspend fun deleteEntireFolder(folderEntity: FolderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteEntireFolder(folderEntity)
        }
    }

    suspend fun deleteResource(resourceEntity: ResourceEntity) {
        repository.deleteResource(resourceEntity)
    }
}