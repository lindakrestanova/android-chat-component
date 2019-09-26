package com.strv.chat.library.core.ui.conversation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.strv.chat.library.core.ui.conversation.adapter.ConversationViewType.CONVERSATION
import com.strv.chat.library.core.ui.conversation.data.ConversationItemView
import com.strv.chat.library.core.ui.conversation.style.ConversationRecyclerViewStyle

class ConversationAdapter(
    private val conversationViewHolderProvider: ConversationViewHolderProvider,
    private val style: ConversationRecyclerViewStyle?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<ConversationItemView>() {

        override fun areItemsTheSame(
            oldItem: ConversationItemView,
            newItem: ConversationItemView
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ConversationItemView,
            newItem: ConversationItemView
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ by lazy { AsyncListDiffer(this, diffUtilCallback) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        conversationViewHolderProvider.holder(parent, viewType, style)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ConversationViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int =
        CONVERSATION.id

    override fun getItemCount(): Int =
        differ.currentList.size

    fun submitList(list: List<ConversationItemView>) {
        differ.submitList(list)
    }

    fun getItem(position: Int): ConversationItemView =
        differ.currentList[position]
}