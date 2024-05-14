package com.hawwas.ulibrary.data.repo

import androidx.lifecycle.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*

class AppDataRepoImpl: AppDataRepo {
    private val subjectsInfoLive = MutableLiveData<List<SubjectHeader>>()
    private val selectedSubjectsLive = MutableLiveData<List<Subject>>()
    private var selectedSubjects = mutableListOf<Subject>()

    init {
        selectedSubjectsLive.observeForever { ls ->
            selectedSubjects = ls.toMutableList()
        }

    }

    override fun updateSubjects(subjects: List<Subject>) {
        selectedSubjectsLive.postValue(subjects)
    }

    override fun getSubjectsInfo(): LiveData<List<SubjectHeader>> {
        return subjectsInfoLive
    }

    override fun updateSubjectsInfo(ls: List<SubjectHeader>) {
        subjectsInfoLive.postValue(ls)
    }

    override fun getSubjectsLive(): LiveData<List<Subject>> {
        return selectedSubjectsLive
    }

    override fun getSubjects(): List<Subject> {
        return selectedSubjects
    }

    override fun updateSubject(subject: Subject) {
        val ls = selectedSubjectsLive.value?.toMutableList() ?: mutableListOf()
        if (ls.contains(subject)) {
            ls.remove(subject)
        }
        ls.add(subject)
        selectedSubjectsLive.postValue(ls)

    }
}