package com.hawwas.ulibrary.data.remote

import okhttp3.*
import retrofit2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

const val repoUrl = "KhaledHawwas/uLibrary/master/"
const val baseUrl="https://raw.githubusercontent.com/"
class RemoteService {
    private val downloadService: GithubDownloadService = Retrofit.Builder()
        .baseUrl(baseUrl)
        .build()
        .create(GithubDownloadService::class.java)

    fun fetchFileFromRepo(remotePath: String, callback: MyCallback) {
        val call = downloadService.downloadFile(repoUrl + remotePath)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val fileResponse = response.body()
                    if (fileResponse != null) {
                        callback.success(fileResponse.string())
                    }
                } else {
                    callback.failure(IOException("Error: " + response.code() + ", " + response.message()
                    +", "+remotePath))
                }
            }

            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                callback.failure(t?: IOException("Unknown error"))
            }
        })
    }

}