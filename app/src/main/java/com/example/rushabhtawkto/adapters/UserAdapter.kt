package com.example.rushabhtawkto.adapters

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rushabhtawkto.R
import com.example.rushabhtawkto.databinding.ItemUserListBinding
import com.example.tawktopractice.data.model.User

class UserAdapter(
    private val onItemClickListener: (User) -> Unit = { _ -> }
) : PagingDataAdapter<User, UserAdapter.UserViewHolder>(
    UserDiffCallback()
) {

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemUserListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    inner class UserViewHolder(
        private val binding: ItemUserListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User?, position: Int) {

            binding.let {
                it.root.setOnClickListener {
                    user?.let { it1 -> onItemClickListener.invoke(it1) }
                }
                val context = it.root.context
                it.tvUsername.text = user?.login ?: ""
                it.tvDetail.text = (user?.type.orEmpty() ?: "") + "-" + (user?.id.toString() ?: "")
                Glide.with(context).load(user?.avatarUrl).placeholder(R.drawable.img_user_profile)
                    .into(it.imgProfile)

                if (position > 0 && position % 3 == 0) {
                    it.imgProfile.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.purple_200
                        ), PorterDuff.Mode.MULTIPLY
                    );
                } else {
                    it.imgProfile.setColorFilter(null);
                }

                if (user?.isNoteAvailable == true) {
                    it.imgNote.isVisible = true
                    Glide.with(context).load(R.drawable.ic_note).into(it.imgNote)
                } else {
                    it.imgNote.isVisible = false
                }
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}