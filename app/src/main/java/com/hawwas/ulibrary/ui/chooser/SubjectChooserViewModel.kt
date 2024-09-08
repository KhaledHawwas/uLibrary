package com.hawwas.ulibrary.ui.chooser

import androidx.lifecycle.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.domain.model.*
import com.hawwas.ulibrary.domain.repo.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

@HiltViewModel
class SubjectChooserViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var appDataRepo: AppDataRepo

    @Inject lateinit var remoteRepo: RemoteRepo

    @Inject lateinit var appStorage: LocalStorage

    @Inject lateinit var databaseRepo: DatabaseRepo

    //TODO
    suspend fun updateHeaders(success: () -> Unit, failure: (Throwable) -> Unit) {
        val headers = databaseRepo.getAllSubjectHeaders()
        if (headers.isNotEmpty() &&
            System.currentTimeMillis() - appStorage.getLastFetchedTime() > day
        ) {
            val subjects = databaseRepo.getAllSubjects()
            for (header in headers) {
                header.selected =
                    !(subjects.firstOrNull { header.remotePath == it.remotePath }?.hidden ?: true)
            }
            appDataRepo.updateHeaders(headers)
            success()
            //TODO: save it in the file

        } else {
            MyLog.d(MyLog.MyTag.NO_HEADERS)
            headersFromRemote(success, failure)

        }

    }

    /**
     * Fetch subjects headers from remote storage and save it to local storage
     */
    private suspend fun headersFromRemote(
        success: () -> Unit,
        e: (Throwable) -> Unit = { MyLog.d(MyLog.MyTag.LAZY_IO, TAG, it.message ?: "null") }
    ): MutableLiveData<List<SubjectHeader>> {
        appStorage.setLastFetchedTime(System.currentTimeMillis())
        remoteRepo.contentFromRepo(RemoteRepo.subjectsHeaderAbs, MyCallback({ data ->
            val headers = toSubjectsHeaderList(data)
            val subjects = databaseRepo.getAllSubjects()
            for (header in headers) {
                header.selected =
                    !(subjects.firstOrNull { header.remotePath == it.remotePath }?.hidden ?: true)
            }
            databaseRepo.upsertSubjectHeaders(headers)
            appDataRepo.updateHeaders(headers)
            success()
            MyLog.d(MyLog.MyTag.HEADERS_FETCHED, TAG, data)
        }, e))
        return appDataRepo.getHeaders()
    }


    fun saveSubjects(headers: List<SubjectHeader>) {
        val selectedHeaders = headers.filter { it.selected }
        for (header in selectedHeaders) {
            remoteRepo.contentFromRepo(header.remotePath, MyCallback({ data ->
                val subject = toSubject(data)
                subject.remotePath = header.remotePath
                MyLog.d(MyLog.MyTag.SAVING_SUBJECT, TAG, subject.name)
                CoroutineScope(Dispatchers.IO).launch {
                    databaseRepo.upsertSubject(subject)
                }
                appDataRepo.updateSubject(subject)
            }))
        }
    }

    companion object {
        private const val TAG = "KH_SubjectChooserVM"
        const val day = 1000 * 60 * 60 * 24
    }

}