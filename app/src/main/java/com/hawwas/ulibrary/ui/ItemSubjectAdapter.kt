package com.hawwas.ulibrary.ui

import android.content.*
import android.util.*
import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import com.hawwas.ulibrary.ui.display.*

class ItemSubjectAdapter(
    var appDataRepo: AppDataRepo,
    var remoteRepo: RemoteRepo,
    val localStorage: LocalStorage,
    var subjects: LiveData<List<Subject>>,
    val lifecycleOwner: LifecycleOwner
):
    RecyclerView.Adapter<ItemSubjectAdapter.ViewHolder>() {
    private lateinit var parent: ViewGroup

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
                localStorage.updateDownloaded(item)
            }
            val downloadedItems = subject.items.count { it.downloaded == DownloadStatus.DOWNLOADED }
            binding.itemsCountTv.text = "${subject.items.size}/${downloadedItems}"
            binding.downloadSubjectBtn.setOnClickListener {
                remoteRepo.downloadSubject(subject)
            }
            binding.downloadSubjectBtn.setImageResource(
                if (downloadedItems == subject.items.size) R.drawable.download_done_24px
                else R.drawable.download_24px
            )
            binding.booksBtn.setOnClickListener {
                Intent(parent.context, ItemsSelectorActivity::class.java).also {
                    it.putExtra("selectedItems", subject.name + "/books")
                    parent.context.startActivity(it)
                }
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
            val index = subjects.value?.indexOfFirst { it.name == p } ?: -1
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
        holder.bind(subjects.value!![position])
    }

    override fun getItemCount(): Int {
        return subjects.value?.size ?: 0.also { Log.d(TAG, "null") }
    }

    companion object {
        private const val TAG = "KH_ItemSubjectAdapter"
    }
}