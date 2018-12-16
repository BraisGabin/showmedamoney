package com.braisgabin.showmedamoney.presentation.amount

import com.braisgabin.showmedamoney.commons.ReducerFactory
import com.braisgabin.showmedamoney.commons.RxViewModel
import com.braisgabin.showmedamoney.presentation.Navigator
import com.braisgabin.showmedamoney.presentation.contacts.Identity
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Named

class AmountPresenter @Inject constructor(
    private val reducerFactory: ReducerFactory,
    private val navigator: Navigator,
    @Named("decimalSeparator") private val decimalSeparator: String
) : RxViewModel() {

  val events: Relay<AmountEvent> = PublishRelay.create()
  val states: Flowable<AmountState> by lazy {
    reducerFactory.create(
        AmountState(allowNextStep = false),
        Flowable.merge(partialStates()))
        .replay(1)
        .autoConnect(0) { disposable.add(it) }
  }

  private fun partialStates(): List<Flowable<out (AmountState) -> AmountState>> {
    return listOf(
        events.ofType(AmountEvent.WriteAmount::class.java)
            .toFlowable(BackpressureStrategy.LATEST)
            .map { NewValuePartialState(it.amount, decimalSeparator) },
        events.ofType(AmountEvent.ClickNextStep::class.java)
            .toFlowable(BackpressureStrategy.LATEST)
            .doOnNext { (amount, contacts) ->
              navigator.step3(BigDecimal(amount.replace(decimalSeparator, ".")), contacts)
            }
            .map {
              Identity<AmountState>()
            }
    )
  }
}
