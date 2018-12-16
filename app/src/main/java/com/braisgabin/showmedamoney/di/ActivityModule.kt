package com.braisgabin.showmedamoney.di

import com.braisgabin.showmedamoney.presentation.Navigator
import com.braisgabin.showmedamoney.presentation.NavigatorForMainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class ActivityModule {

  @Binds
  abstract fun navigatorProvider(navigator: NavigatorForMainActivity): Navigator
}
