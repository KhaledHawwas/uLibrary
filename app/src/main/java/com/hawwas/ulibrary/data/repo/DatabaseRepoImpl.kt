package com.hawwas.ulibrary.data.repo

import android.util.*
import com.hawwas.ulibrary.data.db.*
import com.hawwas.ulibrary.domain.model.*
import com.hawwas.ulibrary.domain.repo.*
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
        for (item in subject.items) {
            upsertItem(item)
        }
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

    override fun getSubjectHeader(subjectName: String): SubjectHeader {
        return database.subjectHeaderDao().getSubjectHeader(subjectName).toSubjectHeader()
    }

    override fun getAllSubjectHeaders(): List<SubjectHeader> {
        return database.subjectHeaderDao().getAllSubjectHeaders().map { it.toSubjectHeader() }
    }

    override fun upsertSubjectHeader(subjectHeader: SubjectHeader) {
        database.subjectHeaderDao().upsert(subjectHeader.toSubjectHeaderEntity())
    }

    override fun upsertSubjectHeaders(subjects: List<SubjectHeader>) {
        database.subjectHeaderDao().upsertAll(subjects.map { it.toSubjectHeaderEntity() })
    }


}