package com.braisgabin.showmedamoney.data

import android.content.ContentResolver
import android.provider.ContactsContract
import arrow.core.Either
import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Mockable
class ContactsDeviceSource @Inject constructor(
    private val contentResolver: ContentResolver
) {
  fun retrieveContacts(): Single<Either<Throwable, List<Contact>>> {
    return Single
        .create<Either<Throwable, List<Contact>>> {
          it.onSuccess(try {
            Either.right(getContactList())
          } catch (e: Throwable) {
            Either.left(e)
          })
        }
        .subscribeOn(Schedulers.io())
  }

  private fun getContactList(): List<Contact> {
    val cursor = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER,
            ContactsContract.Contacts.PHOTO_URI),
        null,
        null,
        null)

    val contacts = mutableListOf<Contact>()
    cursor!!.use {
      val idIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
      val nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
      val hasPhoneNumberIndex = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
      val imageUrlIndex = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
      while (cursor.moveToNext()) {
        val id = cursor.getString(idIndex)!!
        val name = cursor.getString(nameIndex) ?: ""
        val hasPhone = cursor.getInt(hasPhoneNumberIndex)
        val imageUrl = cursor.getString(imageUrlIndex)
        val phone = if (hasPhone > 0) {
          getPhoneNumber(id)
        } else {
          null
        }
        contacts.add(Contact(id, name, imageUrl, phone))
      }
    }
    if (!cursor.isClosed) {
      throw RuntimeException()
    }
    return contacts
  }

  private fun getPhoneNumber(id: String): String? {
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
        arrayOf(id),
        null)

    return cursor!!.use {
      if (cursor.moveToNext()) {
        cursor.getString(0)
      } else {
        null
      }
    }
  }
}
