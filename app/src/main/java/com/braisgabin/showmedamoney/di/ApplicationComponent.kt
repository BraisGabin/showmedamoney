package com.braisgabin.showmedamoney.di

import com.braisgabin.showmedamoney.App
import dagger.BindsInstance
import dagger.Component

@Component
interface ApplicationComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun with(app: App): Builder

    fun build(): ApplicationComponent
  }
}
