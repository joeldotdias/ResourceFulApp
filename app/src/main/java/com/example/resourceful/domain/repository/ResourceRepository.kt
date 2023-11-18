package com.example.resourceful.domain.repository

import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.domain.model.ResourceEntity
import kotlinx.coroutines.flow.Flow

interface ResourceRepository {

    fun getFolders(folderParent: Int): Flow<List<FolderEntity>>

    fun getResources(resourceParent: Int): Flow<List<ResourceEntity>>

    suspend fun addFolder(folderEntity: FolderEntity)

    suspend fun addResource(resourceEntity: ResourceEntity)

    suspend fun deleteFolder(folderEntity: FolderEntity)

    suspend fun deleteResource(resourceEntity: ResourceEntity)
}