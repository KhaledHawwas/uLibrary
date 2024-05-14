package com.hawwas.ulibrary.ui.chooser

import android.view.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.model.*

class SubjectInfoAdapter:
    RecyclerView.Adapter<SubjectInfoAdapter.ViewHolder>() {
    var subjectsInfo: List<SubjectHeader> = emptyList()

    class ViewHolder(private val binding: ItemSubjectInfoBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subjectHeader: SubjectHeader) {
            binding.subjectName.text = subjectHeader.name
            binding.subjectSelectedCB.isChecked = subjectHeader.selected
            binding.subjectSelectedCB.setOnClickListener {
                subjectHeader.selected = binding.subjectSelectedCB.isChecked
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSubjectInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subjectsInfo[position])
    }

    override fun getItemCount(): Int {
        return subjectsInfo.size
    }
}