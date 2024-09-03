package com.hawwas.ulibrary.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_table")
data class SubjectEntity(
    @PrimaryKey val name: String,
    val version: String,
    val remotePath: String,
    var hidden: Boolean = false,
    var totalSize: Long = 0L,

)