package com.hawwas.ulibrary.data.repo

import android.content.*
import com.hawwas.ulibrary.*
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

    override fun getItemSize(item: Item): Long {
        val itemFile = File(appDir, LocalStorage.getItemPath(item))
        if (!itemFile.exists()) {
            MyLog.d(MyLog.MyTag.FILE_SHOULD_EXISTS)
            return 0
        }
        return itemFile.length()
    }

    override fun updateDownloaded(item: Item) {
        val itemFile = File(appDir, LocalStorage.getItemPath(item))
        item.downloaded =
            if (itemFile.exists()) DownloadStatus.DOWNLOADED else DownloadStatus.NOT_STARTED
    }

    override fun saveSubjectData(subject: Subject) {
        val file = File(appDir, LocalStorage.getSubjectFile(subject))
        try {
            file.writeText(subjectToJson(subject))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun saveFile(folderName: String, fileName: String, data: ByteArray) {
        val folder = File(appDir, folderName)
        if (!folder.exists()) {
            folder.mkdirs()
        }
        saveFile(folder.absolutePath + "/" + fileName, data)
    }

    override fun saveFile(fileName: String, data: ByteArray) {
        val file = File(appDir, fileName)
        try {
            file.writeBytes(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun saveCache(folderName: String, fileName: String, data: ByteArray) {
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
        val subjects = mutableListOf<Subject>()
        subjectFiles?.forEach {
            if (!it.isDirectory) {
                return@forEach
            }
            val subjectJson =
                getFileContent(it.absolutePath + "/" + LocalStorage.subjectFile) ?: return@forEach
            val subject = toSubject(subjectJson)
            subjects.add(subject)
            for (item in subject.items) {
                updateDownloaded(item)
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