package com.hawwas.ulibrary.domain.model

enum class DownloadStatus(val code:Int) {
    NOT_STARTED(0),
    DOWNLOADING(1),
    DOWNLOADED(2),
    LOCAL(3),
    FAILED(4), ;

    fun downloadable(): Boolean = this == NOT_STARTED || this == FAILED

    fun exists(): Boolean = this == DOWNLOADED || this == LOCAL
companion object{
    fun getByCode(code: Int): DownloadStatus {
        return entries.first { it.ordinal == code }
    }
}
}
