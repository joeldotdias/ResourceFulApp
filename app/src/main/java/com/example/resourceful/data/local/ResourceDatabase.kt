package com.example.resourceful.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.resourceful.domain.model.FolderEntity
import com.example.resourceful.domain.model.ResourceEntity

@Database(
    entities = [
        FolderEntity::class,
        ResourceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ResourceDatabase: RoomDatabase() {

    abstract val resourceDao: ResourceDao

    companion object {
        const val DATABASE_NAME = "resourceful_db"
    }
}