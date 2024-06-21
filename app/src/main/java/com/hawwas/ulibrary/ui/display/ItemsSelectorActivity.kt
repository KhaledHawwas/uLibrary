package com.hawwas.ulibrary.ui.display

import android.os.*
import android.widget.*
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.core.view.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.R
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import dagger.hilt.android.*
import javax.inject.*

@AndroidEntryPoint
class ItemsSelectorActivity: AppCompatActivity() {
    private lateinit var binding: ActivityItemsSelectorBinding
    @Inject lateinit var remoteRepo: RemoteRepo
    @Inject lateinit var appDataRepo: AppDataRepo
    @Inject lateinit var localStorage: LocalStorage

   private lateinit var selectedSubject: Subject
    private     lateinit var selectedCategory: String
    private     lateinit var selectedItems: List<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemsSelectorBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val extra = intent.getStringExtra("selectedItems")
        try {

            readExtra(extra)
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "Error:${e.message}", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        val itemsDisplayAdapter = ItemsDisplayAdapter(remoteRepo, appDataRepo, this,localStorage)
        itemsDisplayAdapter.items = selectedItems
        binding.subjectTitleTv.text = selectedSubject.name
        binding.categoryTitleTv.text = selectedCategory
        binding.itemsRv.adapter = itemsDisplayAdapter
        binding.itemsRv.layoutManager = LinearLayoutManager(this)
    }


    private fun readExtra(extras: String?) {
        extras?.split("/")?.let { extra ->
            val selectedSubjectName = extra[0]
            selectedCategory = extra[1]
            selectedItems = appDataRepo.getSubjectsLive().value
                ?.find { subject -> subject.name == selectedSubjectName }
                ?.items?.filter { item -> item.category == selectedCategory }
                ?: emptyList()
            selectedSubject = appDataRepo.getSubjectsLive().value!!.firstOrNull {
                it.name == selectedSubjectName
            } ?: throw IllegalArgumentException("Subject not found")
        } ?: throw IllegalArgumentException("Invalid extra")
    }

    override fun onPause() {
        super.onPause()
        localStorage.saveSubjectData(selectedSubject)
    }

    companion object {
        private const val TAG = "KH_ItemsSelectorActivity"
    }
}