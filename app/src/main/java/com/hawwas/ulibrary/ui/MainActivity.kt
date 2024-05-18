package com.hawwas.ulibrary.ui

import android.content.*
import android.os.*
import android.util.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.ui.chooser.*
import dagger.hilt.android.*
import javax.inject.*

private const val TAG = "KH_MainActivity"

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var remoteRepo: RemoteRepo

    @Inject lateinit var localStorage: LocalStorage

    @Inject lateinit var androidDownloader: AndroidDownloader

    @Inject lateinit var appDataRepo: AppDataRepo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(
            savedInstanceState
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemSubjectAdapter = ItemSubjectAdapter(
            appDataRepo, remoteRepo, localStorage, appDataRepo.getSubjectsLive(), this
        )
        binding.subjectsRv.adapter = itemSubjectAdapter
        binding.subjectsRv.layoutManager = LinearLayoutManager(this)

        val subjects = localStorage.loadLocalSubjects()
        Log.d(TAG, "subjects: $subjects")


        if (subjects.isEmpty()) {
            Intent(this@MainActivity, SubjectChooserActivity::class.java).also {
                startActivity(it)
            }
        } else {
            appDataRepo.updateSubjects(subjects)
        }
    }

}