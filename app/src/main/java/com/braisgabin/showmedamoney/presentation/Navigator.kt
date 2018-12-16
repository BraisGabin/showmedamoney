package com.braisgabin.showmedamoney.presentation

import com.braisgabin.showmedamoney.entities.Contact
import java.math.BigDecimal

interface Navigator {
  fun step2(selectedContacts: List<Contact>)

  fun step3(amount: BigDecimal, contacts: List<Contact>)
}
