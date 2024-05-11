package com.hawwas.ulibrary.data.remote

import android.app.*
import android.content.*
import android.net.*
import android.os.*
import java.io.*
import javax.inject.*

class AndroidDownloader @Inject constructor(private val context: Context) {
    private val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun downloadFile(url: String, toPath: String) {
        val destinationDirectory = context.externalMediaDirs.first()
        val destination = File(destinationDirectory, toPath)
        val request = DownloadManager.Request(Uri.parse(url))
            .setDestinationUri(Uri.fromFile(destination))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)

        downloadManager.enqueue(request)
    }
}