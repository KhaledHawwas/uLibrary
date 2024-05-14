package com.hawwas.ulibrary.data.repo

import android.content.*
import android.util.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.domain.repo.LocalStorage.Companion.rootDir
import com.hawwas.ulibrary.domain.repo.LocalStorage.Companion.subjectsDir
import com.hawwas.ulibrary.model.*
import java.io.*

class LocalStorageImpl(context: Context): LocalStorage {
    private val appDir = context.externalMediaDirs.first()!!
    private val cashDir = context.cacheDir!!
    private val dataStoreManager = DataStoreManager(context)

    override fun saveFile(folderName: String, fileName: String, data: ByteArray) {
        val folder = File(appDir, folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val file = File(folder, fileName)
        try {
            file.writeBytes(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun saveCacheFile(folderName: String, fileName: String, data: ByteArray) {
        val folder = File(cashDir, folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val file = File(folder, fileName)
        try {
            file.writeBytes(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun loadLocalSubjects(): List<Subject> {
        val subjectFile = File(appDir, rootDir + subjectsDir)
        val subjectFiles = subjectFile.listFiles()
        Log.d(TAG, "loadLocalSubjects: ${subjectFile.absolutePath}")
        val subjects = mutableListOf<Subject>()
        subjectFiles?.forEach {
            Log.d(TAG, "${it.absolutePath + "//" + LocalStorage.subjectFile}: ")
            if (!it.isDirectory) {
                return@forEach
            }
            val subjectJson =
                getFileContent(it.absolutePath + "//" + LocalStorage.subjectFile) ?: return@forEach
            val subject = toSubject(subjectJson)
            subjects.add(subject)
            for (item in subject.items) {
                val itemFile =
                    File(it, item.getCatalogDir() + item.name)//TODO: check if it is correct
                item.downloaded =
                    if (itemFile.exists()) DownloadStatus.DOWNLOADED else DownloadStatus.FAILED
            }

        }
        return subjects
    }

    override fun getFileContent(path: String): String? {
        val file = File(path)
        if (!file.exists()) {
            return null
        }
        return file.readText()
    }

    override suspend fun saveLastFetchedTime(time: Long) {
        dataStoreManager.saveLastFetchedTime(time)
    }

    override suspend fun getLastFetchedTime(): Long {
        return dataStoreManager.getLastFetchedTime()
    }

    companion object {
        private const val TAG = "KH_LocalStorageImpl"
    }
}