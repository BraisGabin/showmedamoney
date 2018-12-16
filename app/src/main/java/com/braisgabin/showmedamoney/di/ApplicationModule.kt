package com.braisgabin.showmedamoney.di

import android.content.Context
import com.braisgabin.showmedamoney.App
import com.braisgabin.showmedamoney.data.ContactsDataRepository
import com.braisgabin.showmedamoney.domain.ContactsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ApplicationModule {

  @Binds
  abstract fun contextProvider(app: App): Context

  @Binds
  abstract fun contactsRepository(contactsRepository: ContactsDataRepository): ContactsRepository
}
