package com.hawwas.ulibrary.domain.repo

import com.hawwas.ulibrary.domain.model.*

interface DatabaseRepo {
    fun upsertItem(item: Item)

    fun deleteItem(item: Item)

    fun getItemsForSubject(subjectName: String): List<Item>

    fun upsertSubject(subject: Subject)

    fun deleteSubject(subject: Subject)

    fun getSubject(subjectName: String): Subject

    fun getAllSubjects(): List<Subject>

    fun getSubjectHeader(subjectName: String): SubjectHeader

    fun getAllSubjectHeaders(): List<SubjectHeader>

    fun upsertSubjectHeader(subjectHeader: SubjectHeader)

    fun upsertSubjectHeaders(subjects: List<SubjectHeader>)


}