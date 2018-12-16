package com.braisgabin.showmedamoney.presentation.confirmation

import com.braisgabin.showmedamoney.commons.RxViewModel
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Flowable
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ConfirmationPresenter @Inject constructor(
    private val amount: BigDecimal,
    private val contacts: List<Contact>
) : RxViewModel() {

  val states: Flowable<ConfirmationState> by lazy {
    // This is not the perfect split. But it's not that bad.
    val contactsCount = contacts.size
    val split = amount.divide(BigDecimal(contactsCount), 2, RoundingMode.FLOOR)
    val lastSplit = amount.minus(split.times(BigDecimal(contactsCount - 1)))
    val splits = contacts.mapIndexed { i, it ->
      if (i <= contactsCount - 2) {
        Split(split, it)
      } else {
        Split(lastSplit, it)
      }
    }

    Flowable.just(ConfirmationState(splits))
  }
}
