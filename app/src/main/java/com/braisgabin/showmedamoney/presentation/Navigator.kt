package com.braisgabin.showmedamoney.presentation

import com.braisgabin.showmedamoney.entities.Contact

interface Navigator {
  fun step2(selectedContacts: List<Contact>)
}
