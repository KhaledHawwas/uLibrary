package com.hawwas.ulibrary.domain.repo

import android.net.*
import com.hawwas.ulibrary.domain.model.*

interface LocalStorage {

    companion object {
        const val subjectFile = "subject.json"
        const val rootDir = "data/"
        const val headersPath = rootDir + "subjectsInfo.json"
        const val subjectsHeaderFile = "subjectsInfo.json"
        const val subjectsDir = "subjects/"

        fun getItemPath(item: Item): String {
            return rootDir + subjectsDir + item.getPathFromSubjects()
        }

        fun getSubjectFile(subject: Subject): String {
            return rootDir + subjectsDir + subject.name + "/" + subjectFile
        }

    }

    fun loadLocalSubjects(): List<Subject>
    fun saveFile(folderName: String, fileName: String, data: ByteArray)
    fun saveFile( fileName: String, data: ByteArray)
    fun saveCache(folderName: String, fileName: String, data: ByteArray)
    fun getFileContent(path: String): String?
    suspend fun setLastFetchedTime(time: Long)
    suspend fun getLastFetchedTime(): Long
    fun updateFileStatus(item: Item)
    fun getItemSize(item: Item): Long
    fun getFileInfo( uri: Uri): FileInfo
    fun copyItem(filePath: Uri, item: Item)
    fun copyItem(filePath: Uri, item: Item, onFinish: ()->Unit)
    fun getAppDir():String


}