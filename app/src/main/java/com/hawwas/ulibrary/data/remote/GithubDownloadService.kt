package com.hawwas.ulibrary.data.remote

import okhttp3.*
import retrofit2.Call
import retrofit2.http.*

interface GithubDownloadService{
    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>
}
