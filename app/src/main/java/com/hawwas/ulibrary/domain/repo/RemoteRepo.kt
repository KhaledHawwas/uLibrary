package com.hawwas.ulibrary.domain.repo

import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.model.*

interface RemoteRepo {
    companion object {
        fun getItemPath(item: Item): String {
            return baseGithubUrl + repoUrl + item.remotePath
        }

        const val repoUrl = "KhaledHawwas/uLibrary/master/"
        const val baseGithubUrl = "https://raw.githubusercontent.com/"
        const val rootDataDir = "data/"
        const val subjectsHeaderAbs = rootDataDir + "subjectsInfo.json"
    }

    fun contentFromRepo(remotePath: String, callback: MyCallback)
    fun downloadFile(url: String, toPath: String)
    fun downloadSubject(subject: Subject)
    fun downloadItem(item: Item): Boolean
}