package com.braisgabin.showmedamoney.presentation.confirmation

import com.braisgabin.showmedamoney.entities.Contact
import java.math.BigDecimal

data class ConfirmationState(
    val amount: BigDecimal,
    val contacts: List<Split>
)

data class Split(
    val amount: BigDecimal,
    val contact: Contact
)
