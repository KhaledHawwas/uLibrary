package com.hawwas.ulibrary.ui

import android.view.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.*
import com.hawwas.ulibrary.data.remote.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.repo.*
import com.hawwas.ulibrary.model.*
import javax.inject.*

class ItemSubjectAdapter(private val androidDownloader: AndroidDownloader):
    RecyclerView.Adapter<ItemSubjectAdapter.ViewHolder>() {
    var subjects: List<Subject> = emptyList()
    @Inject
    lateinit var appDataRepo: AppDataRepo

    inner class ViewHolder(private val binding: ItemSubjectBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subject: Subject) {
            binding.subjectNameTv.text = subject.name
            val downloadedItems = subject.items.count { it.downloaded == DownloadStatus.DOWNLOADED }
            binding.itemsCountTv.text =
                "${subject.items.size}/${downloadedItems}"
            binding.downloadSubjectBtn.setOnClickListener {
                androidDownloader.downloadSubject(subject)
            }
            binding.downloadSubjectBtn.setImageResource(
                if (downloadedItems == subject.items.size) R.drawable.download_done_24px
                else R.drawable.download_24px
            )

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubjectBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subjects[position])
    }

    override fun getItemCount(): Int {
        return subjects.size
    }
}