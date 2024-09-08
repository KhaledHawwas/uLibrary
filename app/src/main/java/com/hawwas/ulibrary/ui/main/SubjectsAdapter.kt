package com.hawwas.ulibrary.ui.main

import android.content.*
import android.util.*
import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.model.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.ui.display.*

class SubjectsAdapter(
    var appDataRepo: AppDataRepo,
    var remoteRepo: RemoteRepo,
    val localStorage: LocalStorage,
    var subjects: LiveData<List<Subject>>,
    private val lifecycleOwner: LifecycleOwner
): RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {
    private lateinit var parent: ViewGroup
    val filteredSubjects
        get() = subjects.value?.filter { !it.hidden } ?: emptyList()
    init {
        subjects.observe(lifecycleOwner) {
            try {
                notifyDataSetChanged()//TODO: change to notifyItemRangeChanged
            } catch (e: IllegalStateException) {
                Log.e(TAG, "init: ", e)
            }
        }
    }

    inner class ViewHolder(private val binding: ItemSubjectBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subject: Subject) {
            binding.subjectNameTv.text = subject.name
            subject.items.forEach { item ->
                localStorage.updateFileStatus(item)
            }
            val downloadedItems = subject.items.count { it.downloadStatus.exists() }
            binding.itemsCountTv.text = "${downloadedItems}/${subject.items.size}"
            binding.downloadSubjectBtn.setOnClickListener {
                remoteRepo.downloadSubject(subject)
            }
            binding.downloadSubjectBtn.setImageResource(
                if (downloadedItems == subject.items.size) R.drawable.download_done_24px
                else R.drawable.download_24px
            )
            binding.booksBtn.setOnClickListener {
                openCategory(subject.name + "/books")
            }
            binding.examsBtn.setOnClickListener {
                openCategory(subject.name + "/exams")
            }
            binding.lecturesBtn.setOnClickListener {
                openCategory(subject.name + "/lectures")
            }
            binding.sectionsBtn.setOnClickListener {
                openCategory(subject.name + "/sections")
            }
            binding.revisionsBtn.setOnClickListener {
                openCategory(subject.name + "/revision")
            }


        }

        private fun openCategory(category: String) {
            Intent(parent.context, ItemsSelectorActivity::class.java).also {
                it.putExtra("selectedItems", category)
                parent.context.startActivity(it)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.parent = parent
        val binding = ItemSubjectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        appDataRepo.downloadedItem().observe(lifecycleOwner) { path ->
            val p = path.split("/")[0]
            val index = filteredSubjects.indexOfFirst { it.name == p }
            if (index != -1) {
                try {
                    notifyItemChanged(index)
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "onCreateViewHolder: ", e)
                }
            }
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (subjects.value == null) {
            Log.d(TAG, "value is null")
            return
        }
        holder.bind(filteredSubjects[position])
    }

    override fun getItemCount(): Int {
        if (subjects.value == null) {
            MyLog.d(MyLog.MyTag.LAZY_IO, "subjects is null")
            return 0
        }
        return filteredSubjects.size
    }

    companion object {
        private const val TAG = "KH_ItemSubjectAdapter"
    }
}