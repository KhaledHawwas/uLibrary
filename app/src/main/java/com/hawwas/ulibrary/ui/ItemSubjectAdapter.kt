package com.hawwas.ulibrary.ui

import android.view.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.databinding.ItemSubjectBinding
import com.hawwas.ulibrary.model.*

class ItemSubjectAdapter: RecyclerView.Adapter<ItemSubjectAdapter.ViewHolder>() {
    var subjects: List<Subject> = emptyList()

    class ViewHolder(private val binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subject: Subject) {
            binding.subjectNameTv.text = subject.name
            binding.itemsCountTv.text = subject.items.toString()
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