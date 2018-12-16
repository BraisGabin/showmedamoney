package com.braisgabin.showmedamoney.presentation.contacts

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.view.ViewGroup
import com.braisgabin.showmedamoney.presentation.ViewHolder

internal class ContactsAdapter(
    private val listener: Listener
) : ListAdapter<SelectedContact, ViewHolder>(ContactsDiff) {
  private object ContactsDiff : DiffUtil.ItemCallback<SelectedContact>() {
    override fun areItemsTheSame(oldItem: SelectedContact, newItem: SelectedContact): Boolean {
      return oldItem.contact.id == newItem.contact.id
    }

    override fun areContentsTheSame(oldItem: SelectedContact, newItem: SelectedContact): Boolean {
      return oldItem == newItem
    }
  }

  interface Listener {
    fun onClick(contact: SelectedContact)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    val (contact, selected) = item
    holder.bind(
        contact.name,
        contact.phoneNumber ?: "",
        contact.imageUrl,
        { listener.onClick(item) },
        selected)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.create(parent)
  }
}
