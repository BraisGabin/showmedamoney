package com.braisgabin.showmedamoney.di

import com.braisgabin.showmedamoney.App
import com.braisgabin.showmedamoney.presentation.ContactsComponent
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun with(app: App): Builder

    fun build(): ApplicationComponent
  }

  fun contactsBuilder(): ContactsComponent.Builder
}
