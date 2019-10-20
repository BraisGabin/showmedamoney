package com.braisgabin.showmedamoney.di

import androidx.appcompat.app.AppCompatActivity
import com.braisgabin.showmedamoney.App

open class DaggerActivity : AppCompatActivity() {

  val activityComponent: ActivityComponent by lazy {
    (application as App)
        .component
        .activityBuilder()
        .with(this)
        .build()
  }
}
