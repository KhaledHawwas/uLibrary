package com.hawwas.ulibrary.ui.chooser

import androidx.lifecycle.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

@HiltViewModel
class SubjectChooserViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var appDataRepo: AppDataRepo

    @Inject lateinit var remoteRepo: RemoteRepo

    @Inject lateinit var appStorage: LocalStorage

    fun getHeaders(failure: (Throwable) -> Unit): MutableLiveData<List<SubjectHeader>> {
        val content = appStorage.getFileContent(LocalStorage.HeadersAbs)
        if (content != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val time = appStorage.getLastFetchedTime()
                if (System.currentTimeMillis() - time > day) {
                    MyLog.d(MyLog.MyTag.HEADER_OUT_OF_DATE, TAG)
                    headersFromRemote(failure)
                } else {
                    MyLog.d(MyLog.MyTag.SUBJECTS_LOADED, TAG, content)
                    appDataRepo.updateHeaders(toSubjectsHeaderList(content))
                }
                //TODO: save it in the file
            }
            return appDataRepo.getHeaders()
        } else {
            MyLog.d(MyLog.MyTag.NO_HEADERS)
            return headersFromRemote(failure)
        }

    }

    /**
     * Fetch subjects headers from remote storage and save it to local storage
     */
    private fun headersFromRemote(
        e : (Throwable) -> Unit = {MyLog.d(MyLog.MyTag.LAZY_IO, TAG, it.message?:"null")}
    ): MutableLiveData<List<SubjectHeader>> {
        CoroutineScope(Dispatchers.Main).launch {
            appStorage.saveLastFetchedTime(System.currentTimeMillis())
            //TODO: save it in the file
        }
        remoteRepo.contentFromRepo(RemoteRepo.subjectsHeaderAbs, MyCallback({ data ->
            val subjects = toSubjectsHeaderList(data)
            appStorage.saveCache(
                "./", LocalStorage.subjectsHeaderFile, data.toByteArray()
            )
            appDataRepo.updateHeaders(subjects)
            MyLog.d(MyLog.MyTag.HEADERS_FETCHED, TAG, data)
        },e))
        return appDataRepo.getHeaders()
    }

    fun saveSubjects(headers: List<SubjectHeader>) {
        for (subjectInfo in headers) {
            remoteRepo.contentFromRepo(subjectInfo.remotePath, MyCallback({ data ->
                val subject = toSubject(data)
                MyLog.d(MyLog.MyTag.SAVING_SUBJECT, TAG, subject.name)
                appStorage.saveSubjectData(subject)
                appDataRepo.updateSubject(subject)

            }))
        }

    }

    companion object {
        private const val TAG = "KH_SubjectChooserVM"
        const val day = 1000 * 60 * 60 * 24

    }

}