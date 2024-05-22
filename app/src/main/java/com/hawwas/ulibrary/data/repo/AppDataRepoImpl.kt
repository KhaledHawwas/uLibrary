package com.hawwas.ulibrary.data.repo

import androidx.lifecycle.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*

class AppDataRepoImpl: AppDataRepo {
    private val headersLive = MutableLiveData<List<SubjectHeader>>()
    private val selectedSubjectsLive = MutableLiveData<List<Subject>>()
    private val downloadedItemLive = MutableLiveData<String>()

    init {
        selectedSubjectsLive.observeForever {}
        headersLive.observeForever {}
    }

    override fun updateSubjects(subjects: List<Subject>) {
        selectedSubjectsLive.postValue(subjects)
    }

    override fun getHeaders(): MutableLiveData<List<SubjectHeader>> {
        return headersLive
    }

    override fun updateHeaders(ls: List<SubjectHeader>) {
        headersLive.postValue(ls)
    }

    override fun getSubjectsLive(): LiveData<List<Subject>> {
        return selectedSubjectsLive
    }

    override fun downloadedItem(): MutableLiveData<String> {
        return downloadedItemLive
    }


    override fun updateSubject(subject: Subject) {
        val ls = selectedSubjectsLive.value?.toMutableList() ?: mutableListOf()
        val index = ls.indexOf(subject)
        if (index != -1) {
            ls[index] = subject
            selectedSubjectsLive.postValue(ls)
        } else {
            ls.add(subject)
            selectedSubjectsLive.value = ls
        }
    }

    companion object {
        private const val TAG = "KH_AppDataRepoImpl"
    }
}