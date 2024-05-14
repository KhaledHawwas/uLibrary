package com.hawwas.ulibrary.domain.repo

import com.hawwas.ulibrary.model.*

interface LocalStorage {

    companion object {
        const val subjectFile = "subject.json"
        const val rootDir = "data/"
        const val subjectsInfoAbs = rootDir + "subjectsInfo.json"
        const val subjectsHeaderFile = "subjectsInfo.json"
        const val subjectsDir = "subjects/"
    }

    fun loadLocalSubjects(): List<Subject>
    fun saveFile(folderName: String, fileName: String, data: ByteArray)
    fun saveCacheFile(folderName: String, fileName: String, data: ByteArray)
    fun getFileContent(path: String): String?

    suspend fun saveLastFetchedTime(time: Long)
    suspend fun getLastFetchedTime(): Long


}