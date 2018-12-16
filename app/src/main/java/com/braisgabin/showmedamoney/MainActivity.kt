package com.braisgabin.showmedamoney

import android.os.Bundle
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
}
