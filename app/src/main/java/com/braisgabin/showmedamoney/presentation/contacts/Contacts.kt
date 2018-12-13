package com.braisgabin.showmedamoney.presentation.contacts

import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Observable

interface ContactsView {
  fun render(state: ContactsState)

  val events: Observable<ContactsEvent>
}

sealed class ContactsState {
  object Progress : ContactsState()
  object Retry : ContactsState()
  data class Data(
      val contacts: List<SelectedContact>,
      val allowNextStep: Boolean
  ) : ContactsState()
}

data class SelectedContact(
    val contact: Contact,
    val selected: Boolean
)

sealed class ContactsEvent {
  object Retry : ContactsEvent()
  data class ClickContact(val contact: SelectedContact) : ContactsEvent()
  object NextStepClick : ContactsEvent()
}
