package com.hawwas.ulibrary.data.db.entity

import androidx.room.*

@Entity(tableName = "subject_header_table")
data class SubjectHeaderEntity(
    @PrimaryKey
    val name: String,
    val remotePath: String,
)