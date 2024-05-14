package com.hawwas.ulibrary.ui.chooser

import android.os.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.databinding.*
import dagger.hilt.android.*

@AndroidEntryPoint
class SubjectChooserActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySubjectChooserBinding
    private val viewModel: SubjectChooserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectChooserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val subjectInfoAdapter = SubjectInfoAdapter()
        binding.subjectsInfoRC.adapter = subjectInfoAdapter
        binding.subjectsInfoRC.layoutManager = LinearLayoutManager(this)
        viewModel.getSubjectsInfo().observe(this) { subjectsInfo ->
            subjectInfoAdapter.subjectsInfo = subjectsInfo
            subjectInfoAdapter.notifyDataSetChanged()
        }
        binding.saveSubjectsInfoBtn.setOnClickListener {
            viewModel.saveSubjects(subjectInfoAdapter.subjectsInfo)
            finish()
        }
    }

    companion object {
        private const val TAG = "KH_SubjectChooserActivity"
    }
}