package com.braisgabin.showmedamoney.presentation.amount

data class AmountState(
    val allowNextStep: Boolean
)

sealed class ContactsEvent {
  data class WriteAmount(val amount: String) : ContactsEvent()
}
