package com.braisgabin.showmedamoney.presentation

import android.app.Activity
import com.braisgabin.showmedamoney.MainActivity
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.amount.AmountFragment
import com.braisgabin.showmedamoney.presentation.confirmation.ConfirmationFragment
import java.math.BigDecimal
import javax.inject.Inject

class NavigatorForMainActivity @Inject constructor(
    activity: Activity
) : Navigator {

  val activity = activity as MainActivity

  override fun step2(selectedContacts: List<Contact>) {
    activity.moveTo(AmountFragment.create(selectedContacts))
  }

  override fun step3(amount: BigDecimal, contacts: List<Contact>) {
    activity.moveTo(ConfirmationFragment.create(amount, contacts))
  }
}
