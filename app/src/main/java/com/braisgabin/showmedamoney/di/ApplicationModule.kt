package com.braisgabin.showmedamoney.di

import android.content.Context
import com.braisgabin.showmedamoney.App
import com.braisgabin.showmedamoney.data.ContactsDataRepository
import com.braisgabin.showmedamoney.domain.ContactsRepository
import com.braisgabin.showmedamoney.presentation.Navigator
import com.braisgabin.showmedamoney.presentation.NavigatorForMainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {

  @Binds
  abstract fun contextProvider(app: App): Context

  @Binds
  abstract fun navigatorProvider(navigator: NavigatorForMainActivity): Navigator

  @Binds
  abstract fun contactsRepository(contactsRepository: ContactsDataRepository): ContactsRepository
}
