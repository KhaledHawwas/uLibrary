package com.hawwas.ulibrary.ui

import android.content.*
import android.net.*
import android.os.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.ui.chooser.*
import com.hawwas.ulibrary.ui.settings.*
import dagger.hilt.android.*
import javax.inject.*


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
        val subjectsAdapter = SubjectsAdapter(
            appDataRepo, remoteRepo, localStorage, appDataRepo.getSubjectsLive(), this
        )
        binding.subjectsRv.adapter = subjectsAdapter
        binding.subjectsRv.layoutManager = LinearLayoutManager(this)
        binding.notionBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, notionUri))
        }
        binding.settingBtn.setOnClickListener {
            Intent(this, SettingActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.driveBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, driveUri))
        }
        binding.botBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, botUri))
        }
        val subjects = localStorage.loadLocalSubjects()

        if (subjects.isEmpty()) {
            MyLog.d(MyLog.MyTag.NO_SUBJECTS)
            Intent(this@MainActivity, SubjectChooserActivity::class.java).also {
                startActivity(it)
            }
        } else {
            appDataRepo.updateSubjects(subjects)
        }
    }

    companion object {
        private const val TAG = "KH_MainActivity"
        private val notionUri =
            Uri.parse("https://respected-afternoon-b58.notion.site/FCAI-Level-1-second-term-f81b1674cfd248808eaf49a25d8598b3")
        private val driveUri =
            Uri.parse("https://drive.google.com/drive/folders/1SKRh0u48DpNvHQYMJx3tClRmVghspbXL")
        private val botUri = Uri.parse("https://t.me/FCAlFMbot")
    }

}