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

    override suspend fun upsertResource(resourceEntity: ResourceEntity) {
        dao.upsertResource(resourceEntity)
    }

//    override suspend fun updateFolder(folderEntity: FolderEntity) {
//        dao.upsertFolder()
//    }
//
//    override suspend fun updateResource(resourceEntity: ResourceEntity) {
//        TODO("Not yet implemented")
//    }

    override suspend fun deleteFolder(folderEntity: FolderEntity) {
        dao.deleteFolder(folderEntity)
    }

    override suspend fun deleteResource(resourceEntity: ResourceEntity) {
        dao.deleteResource(resourceEntity)
    }

}