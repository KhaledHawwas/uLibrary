package com.hawwas.ulibrary.data.db.entity

import androidx.room.*

@Entity(
    tableName = "item_table",
    primaryKeys = ["name", "subjectName"],
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["name"],
            childColumns = ["subjectName"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ItemEntity(
    @ColumnInfo(index = true) val subjectName: String,
    val name: String,
    val author: String,
    val category: String,
    var version: String,
    val remotePath: String,
    var starred: Boolean = false,
    var lastWatched: Long = 0,
    var downloadStatus: Int
)