package com.braisgabin.showmedamoney

import android.os.Bundle
import android.support.v4.app.Fragment
import com.braisgabin.showmedamoney.di.DaggerActivity
import com.braisgabin.showmedamoney.presentation.contacts.ContactsFragment

class MainActivity : DaggerActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(android.R.id.content, ContactsFragment.create())
          .commit()
    }
  }

  fun moveTo(fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .replace(android.R.id.content, fragment)
        .addToBackStack(null)
        .commit()
  }
}
