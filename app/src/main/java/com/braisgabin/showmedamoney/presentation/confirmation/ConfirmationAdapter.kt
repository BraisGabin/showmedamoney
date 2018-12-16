package com.braisgabin.showmedamoney.presentation.confirmation

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.braisgabin.showmedamoney.presentation.ViewHolder
import java.text.NumberFormat

internal class ConfirmationAdapter : ListAdapter<Split, ViewHolder>(ContactsDiff) {
  private object ContactsDiff : DiffUtil.ItemCallback<Split>() {
    override fun areItemsTheSame(oldItem: Split, newItem: Split): Boolean {
      return oldItem.contact.id == newItem.contact.id
    }

    override fun areContentsTheSame(oldItem: Split, newItem: Split): Boolean {
      return oldItem == newItem
    }
  }

  val format = NumberFormat.getCurrencyInstance()

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    val (split, contact) = item
    holder.bind(
        contact.name,
        format.format(split),
        contact.imageUrl)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.create(parent)
  }
}
