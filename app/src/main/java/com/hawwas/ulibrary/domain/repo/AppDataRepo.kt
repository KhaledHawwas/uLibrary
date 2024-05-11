package com.hawwas.ulibrary.domain.repo

import androidx.lifecycle.LiveData
import com.hawwas.ulibrary.model.*

interface AppDataRepo {
    fun getSubjectsInfo(): LiveData<List<SubjectInfo>>
    fun updateSubjectsInfo(ls:List<SubjectInfo>)
    fun getSubjectsLive(): LiveData<List<Subject>>
    fun getSubjects(): List<Subject>
    fun updateSubject(subject: Subject)
    fun updateSubjects(subjects: List<Subject>)
}