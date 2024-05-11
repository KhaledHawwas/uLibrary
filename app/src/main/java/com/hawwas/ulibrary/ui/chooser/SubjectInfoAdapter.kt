package com.hawwas.ulibrary.ui.chooser

import android.view.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.model.*

class SubjectInfoAdapter :
    RecyclerView.Adapter<SubjectInfoAdapter.ViewHolder>() {
    var subjectsInfo: List<SubjectInfo> = emptyList()

    class ViewHolder(private val binding: ItemSubjectInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subjectInfo: SubjectInfo) {
            binding.subjectName.text = subjectInfo.name
            binding.subjectSelectedSw.isChecked = subjectInfo.selected
            binding.subjectSelectedSw.setOnClickListener {
                subjectInfo.selected = binding.subjectSelectedSw.isChecked
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