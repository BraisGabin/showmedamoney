package com.braisgabin.showmedamoney.presentation.confirmation

import android.os.Bundle
import android.support.v4.app.Fragment
import com.braisgabin.showmedamoney.commons.extensions.toArrayList
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.ContactDTO
import java.math.BigDecimal

class ConfirmationFragment : Fragment() {

  companion object {
    fun create(amount: BigDecimal, contacts: List<Contact>): ConfirmationFragment {
      return ConfirmationFragment().apply {
        arguments = Bundle().apply {
          putString(AMOUNT, amount.toString())
          putParcelableArrayList(CONTACTS, contacts.map { ContactDTO(it) }.toArrayList())
        }
      }
    }
  }
}

private const val AMOUNT = "amount"
private const val CONTACTS = "contacts"
