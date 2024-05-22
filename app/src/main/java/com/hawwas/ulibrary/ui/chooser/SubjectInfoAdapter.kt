package com.hawwas.ulibrary.ui.chooser

import android.view.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.hawwas.ulibrary.databinding.*
import com.hawwas.ulibrary.model.*

class SubjectInfoAdapter(var subjectsInfo: MutableLiveData<List<SubjectHeader>> ):
    RecyclerView.Adapter<SubjectInfoAdapter.ViewHolder>() {
        init{
            subjectsInfo.observeForever {
             try {
                 notifyDataSetChanged()
             }catch (e: Exception){}//TODO: find reason
            }
        }


    inner class ViewHolder(private val binding: ItemSubjectInfoBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subjectHeader: SubjectHeader) {
            binding.subjectName.text = subjectHeader.name
            binding.subjectSelectedCB.isChecked = subjectHeader.selected//TODO: fix
            binding.subjectSelectedCB.setOnClickListener {
                subjectHeader.selected = binding.subjectSelectedCB.isChecked
                subjectsInfo.value = subjectsInfo.value //  to trigger observer
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
        holder.bind(subjectsInfo.value!![position])
    }

    override fun getItemCount(): Int {
        return subjectsInfo.value?.size ?: 0
    }


}