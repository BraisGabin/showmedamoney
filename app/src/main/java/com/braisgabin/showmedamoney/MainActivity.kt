package com.braisgabin.showmedamoney

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.braisgabin.showmedamoney.presentation.ContactsFragment

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .add(android.R.id.content, ContactsFragment.create())
          .commit()
    }
  }
}
