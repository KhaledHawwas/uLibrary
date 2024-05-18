package com.hawwas.ulibrary.data

import android.app.*
import android.content.*
import android.util.*
import com.hawwas.ulibrary.domain.repo.*
import dagger.hilt.android.*
import javax.inject.*

@AndroidEntryPoint
class DownloadReceiver: BroadcastReceiver() {
    companion object {
        private const val TAG = "KH_DownloadReceiver"
    }

    private lateinit var downloadManager: DownloadManager
    @Inject lateinit var appDataRepo: AppDataRepo
    override fun onReceive(context: Context?, intent: Intent?) {
        downloadManager = context?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE != intent?.action) return
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        if (id == -1L) return
        val query = DownloadManager.Query().setFilterById(id)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            val downloadedUriString = cursor.getString(columnIndex)
            Log.d(TAG, "received: $downloadedUriString")
            appDataRepo.downloadedItem()
                .postValue(downloadedUriString.substringAfterLast("subjects/"))
        }

        cursor.close()
    }
}