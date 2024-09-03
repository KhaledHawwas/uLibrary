package com.hawwas.ulibrary.data.db

import com.hawwas.ulibrary.data.db.entity.*
import com.hawwas.ulibrary.domain.model.*

fun Subject.toSubjectEntity(): SubjectEntity {
    return SubjectEntity(
        name = name,
        version = version,
        remotePath = remotePath,
        hidden = hidden,
        totalSize = totalSize
    )
}
fun Item.toItemEntity(): ItemEntity {
    return ItemEntity(
        name = name,
        subjectName = subjectName,
        category = category,
        author = author,
        version = version,
        remotePath = remotePath,
        starred = starred,
        lastWatched = lastWatched,
        downloadStatus = downloadStatus.code
    )
}

fun ItemEntity.toItem(): Item {

    return Item(
        name = name,
        subjectName = subjectName,
        category = category,
        author = author,
        version = version,
        remotePath = remotePath,

    ).apply {
        starred=this@toItem.starred
        lastWatched = this@toItem.lastWatched
        downloadStatus= DownloadStatus.getByCode(this@toItem.downloadStatus)
    }

}
fun SubjectWithItems.toSubject(): Subject {

    return Subject(
        name = subjectEntity.name,
        version = subjectEntity.version,
        remotePath = subjectEntity.remotePath,
        items = itemEntities.map { it.toItem() }.toMutableList()
    ).apply {
        hidden= this@toSubject.subjectEntity.hidden
    }
}
