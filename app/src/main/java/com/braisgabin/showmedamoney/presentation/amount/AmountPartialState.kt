package com.braisgabin.showmedamoney.presentation.amount

import java.math.BigDecimal

data class NewValuePartialState(
    private val amount: String,
    private val decimalSeparator: String
) : (AmountState) -> AmountState {
  override fun invoke(currentState: AmountState): AmountState {
    val string = amount.replace(decimalSeparator, ".")
    val allowNextStep = if (string.isEmpty()) {
      false
    } else {
      BigDecimal(string) > BigDecimal.ZERO
    }
    return AmountState(allowNextStep)
  }
}
