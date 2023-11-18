package com.example.resourceful.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resources")
data class ResourceEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "res_id")
    val id: Int = 0,

    @ColumnInfo(name = "res_title")
    val title: String = "",

    @ColumnInfo(name = "res_link")
    val link: String = "",

    @ColumnInfo(name = "res_parent")
    val parent: Int = 0
)