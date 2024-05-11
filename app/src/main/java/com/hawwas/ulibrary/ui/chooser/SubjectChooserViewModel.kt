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
class SubjectChooserViewModel @Inject constructor(
    private val appStorage: FileStorage
): ViewModel() {
    @Inject lateinit var appDataRepo: AppDataRepo

    @Inject lateinit var remoteService: RemoteService

    @Inject lateinit var dataStore: DataStoreManager


    fun getSubjectsInfo(): LiveData<List<SubjectInfo>> {
        val content = appStorage.getFileContent(FileStorage.subjectsInfoAbs)
        if (content != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val time = dataStore.getLastFetchedTime()
                if (System.currentTimeMillis() - time > day) {
                    Log.d(TAG, "getSubjectsInfo: updating subjects info")
                    fromRemoteStorage()
                } else {
                    Log.d(TAG, "getSubjectsInfo: from local storage")
                    appDataRepo.updateSubjectsInfo(toSubjectsInfoList(content))
                }
            }
            return appDataRepo.getSubjectsInfo()
        } else {
            return fromRemoteStorage()
        }

    }

    /**
     * Fetch subjects info from remote storage and save it to local storage
     */
    private fun fromRemoteStorage(): LiveData<List<SubjectInfo>> {
        Log.d(TAG, "getSubjectsInfo: from remote storage")
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.saveLastFetchedTime(System.currentTimeMillis())
        }
        remoteService.fetchFileFromRepo(FileStorage.subjectsInfoAbs, MyCallback({ data ->
            appStorage.saveFile(
                FileStorage.rootDir,
                FileStorage.subjectsInfoFile,
                data.toByteArray()
            )
            val subjects = toSubjectsInfoList(data)
            for (subject in subjects) {
                Log.d(TAG, "$subjects")
            }
            appDataRepo.updateSubjectsInfo(subjects)
            Log.d(TAG, "getSubjectsInfo: from remote storage success")
        }, { exception ->
            exception.printStackTrace()
        }))
        return appDataRepo.getSubjectsInfo()
    }

    fun saveSubjectsHeaders(subjectsInfo: List<SubjectInfo>) {
        for (subjectInfo in subjectsInfo) {
            remoteService.fetchFileFromRepo(subjectInfo.remotePath, MyCallback({ data ->
                appStorage.saveFile(
                    FileStorage.subjectsDir+subjectInfo.name,FileStorage.subjectFile, data.toByteArray()
                )
                val subject = toSubject(data).apply {
                    filePath = FileStorage.subjectsDir + subjectInfo.name
                }
                appDataRepo.updateSubject(subject)
                Log.d(TAG, "save ${subject.name}: from remote storage success")
            }, { exception ->
                exception.printStackTrace()
            }))
        }

    }


    companion object {
        private const val TAG = "KH_SubjectChooserVM"
    }

}