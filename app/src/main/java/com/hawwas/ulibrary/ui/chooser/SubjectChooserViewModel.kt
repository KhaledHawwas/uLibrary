package com.hawwas.ulibrary.ui.chooser

import android.util.*
import androidx.lifecycle.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import javax.inject.*

const val day = 1000 * 60 * 60 * 24
@HiltViewModel
class SubjectChooserViewModel @Inject constructor(): ViewModel() {
    @Inject lateinit var appDataRepo: AppDataRepo

    @Inject lateinit var remoteRepo: RemoteRepo

    @Inject lateinit var appStorage: LocalStorage

    fun getSubjectsInfo(): LiveData<List<SubjectHeader>> {
        val content = appStorage.getFileContent(LocalStorage.subjectsInfoAbs)
        if (content != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val time = appStorage.getLastFetchedTime()
                if (System.currentTimeMillis() - time > day) {
                    Log.d(TAG, "getSubjectsInfo: updating subjects info")
                    subjectsFromRemote()
                } else {
                    Log.d(TAG, "getSubjectsInfo: from local storage")
                    appDataRepo.updateSubjectsInfo(toSubjectsInfoList(content))
                }
            }
            return appDataRepo.getSubjectsInfo()
        } else {
            return subjectsFromRemote()
        }

    }

    /**
     * Fetch subjects headers from remote storage and save it to local storage
     */
    private fun subjectsFromRemote(): LiveData<List<SubjectHeader>> {
        Log.d(TAG, "getSubjectsInfo: from remote storage")
        CoroutineScope(Dispatchers.Main).launch {
            appStorage.saveLastFetchedTime(System.currentTimeMillis())
        }
        remoteRepo.fetchFileFromRepo(RemoteRepo.subjectsHeaderAbs, MyCallback({ data ->
            appStorage.saveCacheFile(
                "./", LocalStorage.subjectsHeaderFile, data.toByteArray()
            )
            val subjects = toSubjectsInfoList(data)
            Log.d(TAG, "$subjects")
            appDataRepo.updateSubjectsInfo(subjects)
            Log.d(TAG, "getSubjectsInfo: from remote storage success")
        }))
        return appDataRepo.getSubjectsInfo()
    }

    fun saveSubjects(subjectsInfo: List<SubjectHeader>) {
        for (subjectInfo in subjectsInfo) {
            remoteRepo.fetchFileFromRepo(subjectInfo.remotePath, MyCallback({ data ->
                val subject = toSubject(data)
                appStorage.saveFile(
                    LocalStorage.rootDir + LocalStorage.subjectsDir + subject.name,
                    LocalStorage.subjectFile,
                    data.toByteArray()
                )
                appDataRepo.updateSubject(subject)
            }, { exception ->
                exception.printStackTrace()
            }))
        }

    }

    companion object {
        private const val TAG = "KH_SubjectChooserVM"
    }

}