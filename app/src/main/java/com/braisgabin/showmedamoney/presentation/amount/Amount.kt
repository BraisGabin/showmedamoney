package com.braisgabin.showmedamoney.presentation.amount

import com.braisgabin.showmedamoney.entities.Contact

data class AmountState(
    val allowNextStep: Boolean
)

sealed class AmountEvent {
  data class WriteAmount(val amount: String) : AmountEvent()
  data class ClickNextStep(val amount: String, val contacts: List<Contact>) : AmountEvent()
}
