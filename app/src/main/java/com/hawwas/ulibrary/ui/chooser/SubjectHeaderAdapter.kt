package com.hawwas.ulibrary.ui.chooser

import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.domain.model.*
import com.hawwas.ulibrary.domain.repo.*

class SubjectHeaderAdapter(
    var subjectsHeaders: MutableLiveData<List<SubjectHeader>>,
    val appDataRepo: AppDataRepo,

    ):
    RecyclerView.Adapter<SubjectHeaderAdapter.ViewHolder>() {
    init {

        subjectsHeaders.observeForever {
            try {
                notifyDataSetChanged()
            } catch (e: Exception) {
            }//TODO: find reason
        }
    }


    inner class ViewHolder(private val binding: ItemSubjectInfoBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subjectHeader: SubjectHeader) {
            binding.subjectName.text = subjectHeader.name
            //TODO: fix
            binding.subjectSelectedCB.isChecked = subjectHeader.selected
            binding.subjectSelectedCB.setOnClickListener {
                subjectHeader.selected = binding.subjectSelectedCB.isChecked
                subjectsHeaders.value = subjectsHeaders.value //  to trigger observer
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
        holder.bind(subjectsHeaders.value!![position])
    }

    override fun getItemCount(): Int {
        return subjectsHeaders.value?.size ?: 0
    }


}