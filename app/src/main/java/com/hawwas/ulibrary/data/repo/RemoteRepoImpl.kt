package com.hawwas.ulibrary.data.repo

import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import okhttp3.*
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import javax.inject.*

class RemoteRepoImpl @Inject constructor(private var downloader: AndroidDownloader): RemoteRepo {


    private val downloadService: GithubDownloadService = Retrofit.Builder()
        .baseUrl(RemoteRepo.baseGithubUrl)
        .build()
        .create(GithubDownloadService::class.java)

    override fun fetchFileFromRepo(remotePath: String, callback: MyCallback) {
        val call = downloadService.downloadFile(RemoteRepo.repoUrl + remotePath)
        call.enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val fileResponse = response.body()
                    if (fileResponse != null) {
                        callback.success(fileResponse.string())
                    }
                } else {
                    callback.failure(
                        IOException(
                            "Error: " + response.code() + ", " + response.message()
                                    + ", " + remotePath
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                callback.failure(t ?: IOException("Unknown error"))
            }
        })

    }

    override fun downloadFile(url: String, toPath: String) {
        downloader.downloadFile(url, toPath)
    }

    override fun downloadItem(item: Item) {
        if (item.downloaded != DownloadStatus.NOT_STARTED)
            return
        item.downloaded = DownloadStatus.DOWNLOADING
        downloadFile(
            RemoteRepo.getItemPath(item),
            LocalStorage.getItemPath(item)
        )
    }

    override fun downloadSubject(subject: Subject) {
        for (item in subject.items) {
            if (item.downloaded != DownloadStatus.NOT_STARTED)
                continue
            item.downloaded = DownloadStatus.DOWNLOADING
            downloadFile(
                RemoteRepo.baseGithubUrl + RemoteRepo.repoUrl + item.remotePath,
                LocalStorage.getItemPath(item)
            )
        }
    }
}