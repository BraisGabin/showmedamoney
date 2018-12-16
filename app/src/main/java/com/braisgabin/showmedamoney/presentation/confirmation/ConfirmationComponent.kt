package com.braisgabin.showmedamoney.presentation.confirmation

import com.braisgabin.showmedamoney.entities.Contact
import dagger.BindsInstance
import dagger.Subcomponent
import java.math.BigDecimal

@Subcomponent
interface ConfirmationComponent {
  @Subcomponent.Builder
  interface Builder {

    @BindsInstance
    fun with(amount: BigDecimal): Builder
    @BindsInstance
    fun with(contacts: List<Contact>): Builder

    fun build(): ConfirmationComponent

  }

  fun presenter(): ConfirmationPresenter
}
