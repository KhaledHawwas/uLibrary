package com.hawwas.ulibrary.domain.repo

import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.model.*

interface RemoteRepo {
    companion object {
        const val repoUrl = "KhaledHawwas/uLibrary/master/"
        const val baseGithubUrl = "https://raw.githubusercontent.com/"
        const val rootDataDir = "data/"
        const val subjectsHeaderAbs = rootDataDir + "subjectsInfo.json"
    }

    fun fetchFileFromRepo(remotePath: String, callback: MyCallback)
    fun downloadFile(url: String, toPath: String)
    fun downloadSubject(subject: Subject)
}