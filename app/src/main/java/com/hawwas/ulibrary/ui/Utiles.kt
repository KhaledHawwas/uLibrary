package com.hawwas.ulibrary.ui

import java.util.*

fun getMIMEType(extension: String): String {
    return when (extension.lowercase(Locale.getDefault())) {

        "pdf" -> "application/pdf"
        "txt" -> "text/plain"

        "mp4" -> "video/mp4"

        "mp3" -> "audio/mpeg"
        "ogg" -> "audio/ogg"
        "wav" -> "audio/vnd.wave"

        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"

        "doc", "docx" -> "application/msword"
        "xls", "xlsx" -> "application/vnd.ms-excel"
        "ppt", "pptx" -> "application/vnd.ms-powerpoint"

        "zip" -> "application/zip"
        "rar" -> "application/x-rar-compressed"

        else -> "*/*"
    }
}