package com.braisgabin.showmedamoney.presentation.confirmation

import com.braisgabin.showmedamoney.commons.RxViewModel
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Flowable
import java.math.BigDecimal
import javax.inject.Inject

class ConfirmationPresenter @Inject constructor(
    val amount: BigDecimal,
    val contacts: List<Contact>
) : RxViewModel() {

  val states: Flowable<ConfirmationState> by lazy {
    val split = amount.divide(BigDecimal(contacts.size))
    val state = ConfirmationState(contacts.map { Split(split, it) })

    Flowable.just(state)
  }
}
