package com.braisgabin.showmedamoney.presentation

import android.app.Activity
import com.braisgabin.showmedamoney.MainActivity
import com.braisgabin.showmedamoney.entities.Contact
import javax.inject.Inject

class NavigatorForMainActivity @Inject constructor(
    activity: Activity
) : Navigator {

  val activity = activity as MainActivity

  override fun step2(selectedContacts: List<Contact>) {
    activity.moveToAmount(selectedContacts)
  }
}
