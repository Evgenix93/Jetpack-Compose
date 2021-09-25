package com.project.testapp

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.testapp.databinding.UserItemBinding
import okhttp3.internal.notifyAll

class UsersAdapter(private val context: Context, private val onClick: (User) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.Holder>() {

    private var userList = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.user_item), context, onClick)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateList(newList: List<User>) {
        userList = newList
        notifyDataSetChanged()
    }

    class Holder(view: View, private val context: Context, private val onClick: (User) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding: UserItemBinding by viewBinding()

        fun bind(user: User) {
            itemView.setOnClickListener {
                onClick(user)
            }
            binding.nameTextView.text =
                context.getString(R.string.name, user.firstName, user.lastName)
            binding.emailTextView.text = context.getString(R.string.email, user.email)

        }
    }
}