package com.skillbox.github.ui.repository_list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skillbox.github.R
import com.skillbox.github.data.Repository
import com.skillbox.github.data.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repository_item.*

class RepositoryAdapter(val callback: (Int) -> Unit): RecyclerView.Adapter<RepositoryAdapter.Holder>() {
    private var repositoryList = emptyList<Repository>()

    fun update (list: List<Repository>) {
        repositoryList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.repository_item), callback)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(repositoryList[position].id, repositoryList[position].name)
    }

    override fun getItemCount(): Int {
     return  repositoryList.size
    }

    class Holder(override val containerView: View, callback: (Int) -> Unit): RecyclerView.ViewHolder(containerView), LayoutContainer {
        init {
            containerView.setOnClickListener {
              callback(adapterPosition)
            }
        }
        fun bind (id: Int, name: String) {
            repositoryTextView.text = """
                 ID: $id
                 Name: $name
             """.trimIndent()
        }
    }
}