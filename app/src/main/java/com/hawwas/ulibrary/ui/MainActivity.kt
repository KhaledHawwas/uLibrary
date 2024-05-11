package com.hawwas.ulibrary.ui

import android.content.*
import android.os.*
import android.util.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.data.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import com.hawwas.ulibrary.ui.chooser.*
import dagger.hilt.android.*
import javax.inject.*

private const val TAG = "KH_MainActivity"

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var remoteService: RemoteService

    @Inject lateinit var fileStorage: FileStorage

    @Inject lateinit var dataStore: DataStoreManager

    @Inject lateinit var androidDownloader: AndroidDownloader

    @Inject lateinit var appDataRepo: AppDataRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(
            savedInstanceState
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemSubjectAdapter = ItemSubjectAdapter()
        binding.subjectsRv.adapter = itemSubjectAdapter
        binding.subjectsRv.layoutManager = LinearLayoutManager(this)
        appDataRepo.getSubjectsLive().observe(this) {
            itemSubjectAdapter.subjects = it
            itemSubjectAdapter.notifyDataSetChanged()
        }

        val subjects = fileStorage.loadSubjects()

        if (subjects.isEmpty()) {
            Intent(this@MainActivity, SubjectChooserActivity::class.java).also {
                startActivity(it)
            }
        } else {
            appDataRepo.updateSubjects(subjects)
        }

    }

    private fun downloadSubjectsInfo() {
        remoteService.fetchFileFromRepo(
            "data/subjectsInfo.json", MyCallback({
                fileStorage.saveFile(".", "subjectsInfo.json", it.toByteArray())
                appDataRepo.updateSubjectsInfo(toSubjectsInfoList(it))
            }, {
                it.printStackTrace()
                Log.d(TAG, "${it.message}")
            })
        )
    }
}