package com.hawwas.ulibrary.data.repo

import android.content.*
import android.net.*
import android.provider.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.domain.model.*
import com.hawwas.ulibrary.domain.repo.*
import java.io.*
import java.security.*

class LocalStorageImpl(private val context: Context): LocalStorage {
    private val appDir = context.externalMediaDirs.first()!!
    private val cashDir = context.cacheDir!!
    private val dataStoreManager = DataStoreManager(context)

    fun getItemFile(item: Item) = File(appDir, LocalStorage.getItemPath(item))
    override fun getAppDir(): String = appDir.absolutePath
    override fun getFileInfo(uri: Uri): FileInfo {
        var name = ""
        var size: Long = 0

        context.contentResolver.query(
            uri, null, null, null, null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (nameIndex != -1) {
                    name = cursor.getString(nameIndex)
                }
                if (sizeIndex != -1) {
                    size = cursor.getLong(sizeIndex)
                }
            }
        }
        val identifier = if (size < sizeThreshold) {
            "S$size"
        } else {
            "H" + computeFileHash(context.contentResolver, uri)
        }

        return FileInfo(name, size, identifier)
    }

    fun computeFileHash(contentResolver: ContentResolver, uri: Uri): String {
        val digest = MessageDigest.getInstance("SHA-256")
        contentResolver.openInputStream(uri)?.use { inputStream ->
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

    override fun getItemSize(item: Item): Long {
        val itemFile = getItemFile(item)
        if (!itemFile.exists()) {
            MyLog.d(MyLog.MyTag.FILE_SHOULD_EXISTS)
            return 0
        }
        return itemFile.length()
    }


    override fun updateFileStatus(item: Item) {
        if (item.remotePath.isEmpty()) {
            item.downloadStatus = DownloadStatus.LOCAL
            return
        }

        val itemFile = getItemFile(item)
        item.downloadStatus =
            if (itemFile.exists()) DownloadStatus.DOWNLOADED else DownloadStatus.NOT_STARTED
    }

    override fun copyItem(filePath: Uri, item: Item) {
        copyItem(filePath, item) {}
    }

    override fun copyItem(filePath: Uri, item: Item, onFinish: () -> Unit) {
        context.contentResolver.openInputStream(filePath)?.use { inputStream ->
            val itemFile = getItemFile(item)
            itemFile.parentFile?.mkdirs()
            itemFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        onFinish()
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
    /*
    before room
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
                updateFileStatus(item)
            }

        }
        return subjects
    }
*/
    override fun getFileContent(path: String): String? {
        val file = File(path)
        if (!file.exists()) {
            return null
        }
        return file.readText()
    }

    override suspend fun setLastFetchedTime(time: Long) {
        dataStoreManager.saveLastFetchedTime(time)
    }

    override suspend fun getLastFetchedTime(): Long {
        return dataStoreManager.getLastFetchedTime()
    }

    companion object {
        private const val TAG = "KH_LocalStorageImpl"
        const val sizeThreshold = 1024 * 1024 * 10 //10MB
    }
}