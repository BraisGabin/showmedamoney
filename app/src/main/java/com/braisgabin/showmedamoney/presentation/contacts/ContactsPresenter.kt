package com.braisgabin.showmedamoney.presentation.contacts

import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.commons.ReducerFactory
import com.braisgabin.showmedamoney.commons.RxViewModel
import com.braisgabin.showmedamoney.domain.GetContactsUseCase
import com.braisgabin.showmedamoney.presentation.Navigator
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Inject

@Mockable
class ContactsPresenter @Inject constructor(
    private val reducerFactory: ReducerFactory,
    private val getContactsUseCase: GetContactsUseCase,
    private val navigator: Navigator
) : RxViewModel() {
  val events: Relay<ContactsEvent> = PublishRelay.create()
  val states: Flowable<ContactsState> by lazy {
    reducerFactory.create(
        ContactsState.Progress,
        Flowable.merge(partialStates()))
        .doOnNext { lastState = it }
        .replay(1)
        .autoConnect(0) { disposable.add(it) }
  }

  private var lastState: ContactsState? = null

  private fun partialStates(): List<Flowable<out (ContactsState) -> ContactsState>> {
    return listOf(
        getContactsUseCase.retrieveContacts()
            .toFlowable()
            .map { response -> RetrieveDataPartialState(response) },
        events.ofType(ContactsEvent.Retry::class.java)
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMapSingle { getContactsUseCase.retrieveContacts() }
            .map { response -> RetrieveDataPartialState(response) },
        events.ofType(ContactsEvent.ClickContact::class.java)
            .toFlowable(BackpressureStrategy.BUFFER)
            .map { (clickedContact) -> ClickContactPartialState(clickedContact) },
        events.ofType(ContactsEvent.NextStepClick::class.java)
            .toFlowable(BackpressureStrategy.LATEST)
            .doOnNext { _ ->
              val selectedContacts = (lastState as? ContactsState.Data)?.let { state ->
                state.contacts
                    .filter { it.selected }
                    .map { it.contact }
              } ?: emptyList()
              navigator.step2(selectedContacts)
            }
            .map { Identity<ContactsState>() }
    )
  }
}
