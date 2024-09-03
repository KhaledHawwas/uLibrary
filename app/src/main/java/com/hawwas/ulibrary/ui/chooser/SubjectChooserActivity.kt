package com.hawwas.ulibrary.ui.chooser

import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.R
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.model.*
import com.hawwas.ulibrary.domain.repo.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import retrofit2.*
import java.net.*
import javax.inject.*

@AndroidEntryPoint
class SubjectChooserActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySubjectChooserBinding
    private val viewModel: SubjectChooserViewModel by viewModels()
    @Inject lateinit var appRepo: AppDataRepo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectChooserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.Main).launch { getData() }
        val headers = appRepo.getHeaders()
        val subjectInfoAdapter = SubjectInfoAdapter(headers, appRepo)
        binding.subjectsInfoRC.adapter = subjectInfoAdapter
        binding.subjectsInfoRC.layoutManager = LinearLayoutManager(this)
        binding.saveSubjectsInfoBtn.isEnabled = false
        headers.observe(this) { list ->
            binding.saveSubjectsInfoBtn.isEnabled = list.any { it.selected }
        }
        binding.refreshBtn.isEnabled = false
        binding.refreshBtn.setOnClickListener {
            binding.refreshBtn.isEnabled = false
            CoroutineScope(Dispatchers.Main).launch { getData() }
        }
        binding.saveSubjectsInfoBtn.setOnClickListener {
            viewModel.saveSubjects(headers.value!!.filter { it.selected })
            finish()
        }
    }

    private suspend fun getData(): MutableLiveData<List<SubjectHeader>> {
        binding.chooserProgBar.visibility = View.VISIBLE
        return viewModel.getHeaders {
            binding.refreshBtn.isEnabled = true
            binding.chooserProgBar.visibility = View.GONE
            if (it is UnknownHostException || it is SocketTimeoutException) {
                Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                    .show()
                MyLog.d(MyLog.MyTag.NO_INTERNET, TAG, it.message ?: "null")
            } else if (it is HttpException && it.code() == 403) {
                Toast.makeText(
                    this,
                    getString(R.string.you_reach_your_limit_try_again_later), Toast.LENGTH_LONG
                ).show()
                MyLog.d(MyLog.MyTag.TIME_LIMIT, TAG, it.message ?: "null")
            } else {
                Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                MyLog.d(MyLog.MyTag.UNKNOWN_ERROR, TAG, it.message ?: "null")
            }
        }.also { binding.chooserProgBar.visibility = View.GONE }
    }


    companion object {
        private const val TAG = "KH_SubjectChooserActivity"
    }
}