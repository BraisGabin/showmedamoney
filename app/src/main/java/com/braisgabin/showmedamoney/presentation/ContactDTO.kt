package com.braisgabin.showmedamoney.presentation

import android.os.Parcelable
import com.braisgabin.showmedamoney.entities.Contact
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContactDTO(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val phoneNumber: String?
) : Parcelable {

  constructor(contact: Contact) : this(
      contact.id,
      contact.name,
      contact.imageUrl,
      contact.phoneNumber
  )

  fun toDomain() = Contact(id, name, imageUrl, phoneNumber)
}
