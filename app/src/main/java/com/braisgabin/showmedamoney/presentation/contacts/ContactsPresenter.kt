package com.braisgabin.showmedamoney.presentation.contacts

import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.commons.OnlyOnce
import com.braisgabin.showmedamoney.commons.ReducerFactory
import com.braisgabin.showmedamoney.commons.RxViewModel
import com.braisgabin.showmedamoney.domain.GetContactsUseCase
import com.braisgabin.showmedamoney.presentation.Navigator
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

@Mockable
class ContactsPresenter @Inject constructor(
    view: ContactsView,
    private val reducerFactory: ReducerFactory,
    private val getContactsUseCase: GetContactsUseCase,
    private val navigator: Navigator
) : RxViewModel() {
  private val events: Observable<ContactsEvent> = view.events
  private val render: (ContactsState) -> Unit = view::render
  private val onlyOnce = OnlyOnce()

  private var lastState: ContactsState? = null

  fun start() {
    onlyOnce.checkOnlyOnce()

    disposable.add(reducerFactory.create(
        ContactsState.Progress,
        Flowable.merge(partialStates())
    ) { state ->
      render(state)
      lastState = state
    })
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
