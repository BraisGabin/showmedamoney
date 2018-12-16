package com.braisgabin.showmedamoney.presentation

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.braisgabin.showmedamoney.R
import com.squareup.picasso.Picasso
import kotterknife.bindView

internal class ViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {
  private val imageView: ImageView by bindView(R.id.imageView)
  private val nameView: TextView by bindView(R.id.nameView)
  private val phoneView: TextView by bindView(R.id.phoneView)

  fun bind(
      title: String,
      subTitle: String,
      imageUrl: String?,
      listener: (() -> Unit)? = null,
      selected: Boolean = false
  ) {
    itemView.setOnClickListener(if (listener == null) {
      null
    } else {
      View.OnClickListener { listener() }
    })
    nameView.text = title
    phoneView.text = subTitle
    Picasso.get()
        .load(imageUrl)
        .fit()
        .centerCrop()
        .into(imageView)
    itemView.isActivated = selected
  }

  companion object {
    fun create(parent: ViewGroup): ViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)

      return ViewHolder(view)
    }
  }
}
