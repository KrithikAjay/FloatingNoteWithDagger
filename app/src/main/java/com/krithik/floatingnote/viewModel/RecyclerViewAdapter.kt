package com.krithik.floatingnote.viewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.krithik.floatingnote.database.Note
import com.krithik.floatingnote.databinding.ListItemBinding


class RecyclerViewAdapter(private val listener: RowClickListener) : ListAdapter<Note, RecyclerViewAdapter.ViewHolder>(DiffUtilItemCallBack) {
    object DiffUtilItemCallBack : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {

            return oldItem == newItem
        }

    }

    class ViewHolder(private val binding: ListItemBinding, private val listener: RowClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.note = note
            binding.deleteButton.setOnClickListener {
                listener.onDeleteNote(note)
            }


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder((ListItemBinding.inflate((layoutInflater))), listener)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)

    }

    interface RowClickListener {
        fun onDeleteNote(note: Note)


    }


}



