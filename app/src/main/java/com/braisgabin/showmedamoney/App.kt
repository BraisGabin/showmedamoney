package com.braisgabin.showmedamoney

import android.app.Application
import com.braisgabin.showmedamoney.di.ApplicationComponent
import com.braisgabin.showmedamoney.di.DaggerApplicationComponent

class App : Application() {

  val component: ApplicationComponent by lazy {
    DaggerApplicationComponent.builder()
        .with(this)
        .build()
  }
}

inline val Application.component: ApplicationComponent
  get() = (this as App).component
