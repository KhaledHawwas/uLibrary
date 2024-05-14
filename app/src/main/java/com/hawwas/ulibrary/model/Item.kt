package com.hawwas.ulibrary.model

data class Item(
    val name: String,
    val author: String,
    val catalog: String,
    var version: String,
    val remotePath: String
) {
    var stared: Boolean = false
    var lastWatched: Long = 0
    var filePath: String = ""//TODO
    var downloaded: DownloadStatus = DownloadStatus.NOT_STARTED
    fun getCatalogDir(): String {
        return when (catalog) {
            "sections" -> "sections/"
            else -> "$catalog/"
        }
    }
}

enum class DownloadStatus {
    NOT_STARTED,
    DOWNLOADING,
    DOWNLOADED,
    FAILED
}
