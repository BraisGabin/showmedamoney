package com.braisgabin.showmedamoney.di

import com.braisgabin.showmedamoney.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent {

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun with(app: App): Builder

    fun build(): ApplicationComponent
  }

  fun activityBuilder(): ActivityComponent.Builder
}
