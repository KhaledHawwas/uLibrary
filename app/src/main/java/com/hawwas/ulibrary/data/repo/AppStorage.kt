package com.hawwas.ulibrary.data.repo

import android.content.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.domain.repo.FileStorage.Companion.subjectsDir
import com.hawwas.ulibrary.model.*
import java.io.*

class AppStorage(context: Context): FileStorage {
    val appDir = context.externalMediaDirs.first()
    val cashDir = context.cacheDir

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

    override fun loadSubjects(): List<Subject> {
        val subjectFolder = File(appDir, subjectsDir).listFiles()
        val subjects = mutableListOf<Subject>()
        subjectFolder?.forEach {
            val subject = getFileContent(it.absolutePath + FileStorage.subjectFile)
            if (subject != null) {
                subjects.add(toSubject(subject))
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
}