package com.braisgabin.showmedamoney.di

import android.app.Activity
import com.braisgabin.showmedamoney.presentation.amount.AmountFragment
import com.braisgabin.showmedamoney.presentation.amount.AmountPresenter
import com.braisgabin.showmedamoney.presentation.contacts.ContactsPresenter
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    fun with(activity: Activity): Builder

    fun build(): ActivityComponent
  }

  fun inject(fragment: AmountFragment)

  fun contactsPresenter(): ContactsPresenter
  fun amountPresenter(): AmountPresenter
}
