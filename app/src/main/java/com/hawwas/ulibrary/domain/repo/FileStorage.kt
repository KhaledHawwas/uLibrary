package com.hawwas.ulibrary.domain.repo

import com.hawwas.ulibrary.model.*

interface FileStorage {

    companion object {
        const val subjectFile = "subject.json"
        const val rootDir = "data/"
        const val subjectsInfoAbs = rootDir + "subjectsInfo.json"
        const val subjectsInfoFile = "subjectsInfo.json"
        const val subjectsDir = rootDir + "subjects/"
    }

    fun loadSubjects(): List<Subject>
    fun saveFile(folderName:String, fileName: String, data: ByteArray)
    fun saveCacheFile(folderName:String,fileName: String, data: ByteArray)
    fun getFileContent(path: String): String?


}