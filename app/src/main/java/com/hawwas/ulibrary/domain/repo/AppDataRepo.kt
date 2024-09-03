package com.hawwas.ulibrary.domain.repo

import androidx.lifecycle.*
import com.hawwas.ulibrary.domain.model.*

interface AppDataRepo {
    fun getHeaders(): MutableLiveData<List<SubjectHeader>>
    fun updateHeaders(ls: List<SubjectHeader>)
    fun getSubjectsLive(): LiveData<List<Subject>>
    fun updateSubject(subject: Subject)
    fun updateSubjects(subjects: List<Subject>)
    fun downloadedItem(): MutableLiveData<String>
}