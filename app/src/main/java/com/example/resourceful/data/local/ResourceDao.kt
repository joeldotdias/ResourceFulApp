package com.example.resourceful.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.domain.model.ResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ResourceDao {

    @Query("SELECT * FROM folders WHERE fld_parent = :folderParent")
    fun getFolders(folderParent: Int): Flow<List<FolderEntity>>

    @Query("SELECT * FROM resources WHERE res_parent = :resourceParent")
    fun getResources(resourceParent: Int): Flow<List<ResourceEntity>>

    @Upsert
    suspend fun upsertFolder(folderEntity: FolderEntity)

    @Upsert
    suspend fun upsertResource(resourceEntity: ResourceEntity)

    @Delete
    suspend fun deleteFolder(folderEntity: FolderEntity)

    @Delete
    suspend fun deleteResource(resourceEntity: ResourceEntity)
}