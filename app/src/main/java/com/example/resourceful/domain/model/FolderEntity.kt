package com.example.resourceful.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class FolderEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fld_id")
    val id: Int = 0,

    @ColumnInfo(name = "fld_name")
    var name: String = "",

    @ColumnInfo(name = "fld_parent")
    var parent: Int = 0
)