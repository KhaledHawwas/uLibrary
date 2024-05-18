package com.hawwas.ulibrary.domain.repo

import androidx.lifecycle.*
import com.hawwas.ulibrary.model.*

interface AppDataRepo {
    fun getSubjectsInfo(): LiveData<List<SubjectHeader>>
    fun updateSubjectsInfo(ls: List<SubjectHeader>)
    fun getSubjectsLive(): LiveData<List<Subject>>
    fun updateSubject(subject: Subject)
    fun updateSubjects(subjects: List<Subject>)
    fun downloadedItem(): MutableLiveData<String>
}