package com.hawwas.ulibrary.data.remote

import android.app.*
import android.content.*
import android.net.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.domain.repo.RemoteRepo.Companion.baseGithubUrl
import com.hawwas.ulibrary.domain.repo.RemoteRepo.Companion.repoUrl
import com.hawwas.ulibrary.model.*
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

    fun downloadSubject(subject: Subject) {
        for (item in subject.items) {
            if (item.downloaded != DownloadStatus.NOT_STARTED)
                continue
            downloadFile(
                baseGithubUrl + repoUrl + item.remotePath,
                LocalStorage.rootDir + LocalStorage.subjectsDir + item.filePath
            )
        }
    }
}