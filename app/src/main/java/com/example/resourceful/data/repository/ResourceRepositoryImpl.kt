package com.example.resourceful.data.repository

import com.example.resourceful.data.local.ResourceDao
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.domain.model.ResourceEntity
import com.example.resourceful.domain.repository.ResourceRepository
import kotlinx.coroutines.flow.Flow

class ResourceRepositoryImpl(
    private val dao: ResourceDao
): ResourceRepository {

    override fun getFolders(folderParent: Int): Flow<List<FolderEntity>> {
        return dao.getFolders(folderParent)
    }

    override fun getResources(resourceParent: Int): Flow<List<ResourceEntity>> {
        return dao.getResources(resourceParent)
    }

    override suspend fun upsertFolder(folderEntity: FolderEntity) {
        dao.upsertFolder(folderEntity)
    }

    override suspend fun deleteEntireFolder(folder: FolderEntity) {
        val nextOfKinFolders = dao.getDescendantFolders(folder.id)
        val nextOfKinResources = dao.getDescendantResources(folder.id)

        nextOfKinResources.forEach { resource ->
            dao.deleteResource(resource)
        }

        dao.deleteFolder(folder)
        while(nextOfKinFolders.isNotEmpty()) {
            nextOfKinFolders.forEach { fld ->
                deleteEntireFolder(fld)
            }
        }
    }

    override suspend fun upsertResource(resourceEntity: ResourceEntity) {
        dao.upsertResource(resourceEntity)
    }

    override suspend fun deleteResource(resourceEntity: ResourceEntity) {
        dao.deleteResource(resourceEntity)
    }
}