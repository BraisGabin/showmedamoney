package com.braisgabin.showmedamoney.presentation

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.braisgabin.showmedamoney.R
import com.squareup.picasso.Picasso
import kotterknife.bindView

internal class ContactsAdapter(
    private val listener: Listener
) : ListAdapter<SelectedContact, ContactsAdapter.ViewHolder>(ContactsDiff) {
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
    holder.bind(getItem(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.create(parent, listener)
  }

  internal class ViewHolder(
      view: View,
      private val listener: Listener
  ) : RecyclerView.ViewHolder(view) {
    private val imageView: ImageView by bindView(R.id.imageView)
    private val nameView: TextView by bindView(R.id.nameView)
    private val phoneView: TextView by bindView(R.id.phoneView)

    fun bind(item: SelectedContact) {
      itemView.setOnClickListener {
        listener.onClick(item)
      }
      nameView.text = item.contact.name
      phoneView.text = item.contact.phoneNumber
      Picasso.get()
          .load(item.contact.imageUrl)
          .fit()
          .centerCrop()
          .into(imageView)
      itemView.isActivated = item.selected
    }

    companion object {
      fun create(parent: ViewGroup, listener: Listener): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)

        return ViewHolder(view, listener)
      }
    }
  }
}
