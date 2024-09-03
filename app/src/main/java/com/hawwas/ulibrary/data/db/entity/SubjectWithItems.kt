package com.hawwas.ulibrary.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SubjectWithItems(
    @Embedded val subjectEntity: SubjectEntity,
    @Relation(
        parentColumn = "name",
        entityColumn = "subjectName"
    )
    val itemEntities: List<ItemEntity>
)