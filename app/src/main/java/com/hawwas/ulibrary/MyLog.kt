package com.hawwas.ulibrary

import android.util.*

object MyLog {
    const val TAG = "KH"
    fun d(myTag: MyTag, tag: String = "", message: String = "") {
        when (myTag) {
            MyTag.NO_SUBJECTS -> Log.d(
                TAG,
                "${myTag.name}/No subjects found in local storage. Redirecting to SubjectChooserActivity."
            )

            MyTag.SUBJECTS_LOADED -> Log.d(tag, "${myTag.name}/$message")
            MyTag.NO_HEADERS -> Log.d(
                TAG,
                "${myTag.name}/No headers found in local storage. Fetching from remote storage."
            )

            MyTag.SAVING_SUBJECT -> Log.d(tag, "${myTag.name}/$message")
            MyTag.HEADERS_FETCHED -> Log.d(tag, "${myTag.name}/$message")
            MyTag.LAZY_IO -> Log.d(TAG, "${myTag.name}/$message")
            MyTag.DOWNLOAD_COMPLETE -> Log.d(tag, "received: $message")
            MyTag.UNKNOWN_ERROR -> Log.d(TAG, "unknown error")
            MyTag.HEADER_OUT_OF_DATE -> Log.d(TAG, "header out of date")
            MyTag.NO_INTERNET -> Log.d(TAG, "no internet")
            MyTag.TIME_LIMIT -> Log.d(TAG, "time limit")
            MyTag.FILE_SHOULD_EXISTS->Log.d(TAG,"file should exists")
            else -> Log.d(myTag.name, message)
        }
    }

    enum class MyTag {
        NO_HEADERS,
        SUBJECTS_LOADED,
        DOWNLOAD_COMPLETE,
        HEADER_OUT_OF_DATE,
        NO_SUBJECTS,
        HEADERS_FETCHED,
        SAVING_SUBJECT,
        LAZY_IO,
        NO_INTERNET,
        TIME_LIMIT,
        UNKNOWN_ERROR,
        FILE_SHOULD_EXISTS,

    }
}