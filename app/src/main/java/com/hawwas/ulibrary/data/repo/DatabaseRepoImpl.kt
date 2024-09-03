package com.hawwas.ulibrary.data.repo

import com.hawwas.ulibrary.data.db.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import com.hawwas.ulibrary.data.db.*
import com.hawwas.ulibrary.data.db.entity.*
import javax.inject.*

class DatabaseRepoImpl @Inject constructor(
    private val database: MyDatabase
): DatabaseRepo {
    override fun upsertItem(item: Item) {
        database.itemDao().upsertItem(item.toItemEntity())
    }

    override fun deleteItem(item: Item) {
        database.itemDao().deleteItem(item.toItemEntity())
    }

    override fun getItemsForSubject(subjectName: String): List<Item> {
        return database.itemDao().getItemsForSubject(subjectName).map { it.toItem() }
    }

    override fun upsertSubject(subject: Subject) {
        database.subjectDao().upsertSubject(subject.toSubjectEntity())
    }

    override fun deleteSubject(subject: Subject) {
        database.subjectDao().deleteSubject(subject.toSubjectEntity())
    }

    override fun getSubject(subjectName: String): Subject {
        return database.subjectDao().getSubjectWithItems(subjectName).toSubject()
    }

    override fun getAllSubjects(): List<Subject> {
        return database.subjectDao().getAllSubjects().map { it.toSubject() }
    }


}