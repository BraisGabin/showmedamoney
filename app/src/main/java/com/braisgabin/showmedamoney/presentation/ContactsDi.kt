package com.braisgabin.showmedamoney.presentation

import android.arch.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface ContactsComponent {
  fun inject(fragment: ContactsFragment)

  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    fun withView(view: ContactsView): Builder

    @BindsInstance
    fun withLifecycle(lifecycle: Lifecycle): Builder

    fun build(): ContactsComponent
  }
}
