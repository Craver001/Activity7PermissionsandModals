package com.example.activity7permissionsandmodals.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.activity7permissionsandmodals.R

class SubjectViewHolder(private val subList: List<String>) :
    RecyclerView.Adapter<SubjectViewHolder.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.r_subject_design, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return subList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sub = subList[position]
        holder.subjectTextView.text = sub
    }
}
