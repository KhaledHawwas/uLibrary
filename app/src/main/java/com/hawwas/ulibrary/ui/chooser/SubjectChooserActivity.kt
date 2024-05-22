package com.hawwas.ulibrary.ui.chooser

import android.os.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.R
import com.hawwas.ulibrary.databinding.*
import dagger.hilt.android.*
import retrofit2.*
import java.net.*

@AndroidEntryPoint
class SubjectChooserActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySubjectChooserBinding
    private val viewModel: SubjectChooserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectChooserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val headers = getData()
        val subjectInfoAdapter = SubjectInfoAdapter(headers)
        binding.subjectsInfoRC.adapter = subjectInfoAdapter
        binding.subjectsInfoRC.layoutManager = LinearLayoutManager(this)
        binding.saveSubjectsInfoBtn.isEnabled=false
        headers.observe(this) { list ->
            binding.saveSubjectsInfoBtn.isEnabled = list.any { it.selected }
        }
        binding.refreshBtn.isEnabled = false
        binding.refreshBtn.setOnClickListener {
            getData()
            binding.refreshBtn.isEnabled = false
        }
        binding.saveSubjectsInfoBtn.setOnClickListener {
            viewModel.saveSubjects(headers.value!!.filter { it.selected })
            finish()
        }
    }

    private fun getData() = viewModel.getHeaders {
        binding.refreshBtn.isEnabled = true
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
            Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT)
                .show()
            MyLog.d(MyLog.MyTag.UNKNOWN_ERROR, TAG, it.message ?: "null")
        }
    }


    companion object {
        private const val TAG = "KH_SubjectChooserActivity"
    }
}