package com.braisgabin.showmedamoney.presentation

import android.app.Activity
import com.braisgabin.showmedamoney.MainActivity
import com.braisgabin.showmedamoney.entities.Contact
import java.math.BigDecimal
import javax.inject.Inject

class NavigatorForMainActivity @Inject constructor(
    activity: Activity
) : Navigator {

  val activity = activity as MainActivity

  override fun step2(selectedContacts: List<Contact>) {
    activity.moveToAmount(selectedContacts)
  }

  override fun step3(amount: BigDecimal, contacts: List<Contact>) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
