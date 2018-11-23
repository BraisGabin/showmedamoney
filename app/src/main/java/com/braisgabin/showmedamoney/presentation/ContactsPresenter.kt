package com.braisgabin.showmedamoney.presentation

import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.commons.OnlyOnce
import com.braisgabin.showmedamoney.commons.ReducerFactory
import com.braisgabin.showmedamoney.commons.RxViewModel
import com.braisgabin.showmedamoney.domain.GetContactsUseCase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable

@Mockable
class ContactsPresenter(
    private val events: Observable<ContactsEvent>,
    private val render: (ContactsState) -> Unit,
    private val reducerFactory: ReducerFactory,
    private val getContactsUseCase: GetContactsUseCase,
    private val navigator: Navigator
) : RxViewModel() {
  private val onlyOnce = OnlyOnce()

  fun start() {
    onlyOnce.checkOnlyOnce()

    disposable.add(reducerFactory.create(
        ContactsState.Progress,
        Flowable.merge(partialStates()),
        render))
  }

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
            .doOnNext { navigator.step2() }
            .map { Identity<ContactsState>() }
    )
  }
}
