package com.braisgabin.showmedamoney.presentation.contacts

import arrow.core.Either
import com.braisgabin.showmedamoney.entities.Contact

data class RetrieveDataPartialState(
    private val response: Either<Throwable, List<Contact>>
) : (ContactsState) -> ContactsState {
  override fun invoke(currentState: ContactsState): ContactsState {
    return response.fold(
        { ContactsState.Retry },
        { data ->
          ContactsState.Data(data.map { SelectedContact(it, false) }, false)
        })
  }
}

data class ClickContactPartialState(
    private val clickedContact: SelectedContact
) : (ContactsState) -> ContactsState {
  override fun invoke(currentState: ContactsState): ContactsState {
    return when (currentState) {
      is ContactsState.Data -> {
        val data = currentState.contacts.map {
          if (it == clickedContact) it.copy(selected = !it.selected) else it
        }
        ContactsState.Data(data, data.any { it.selected })
      }
      ContactsState.Progress,
      ContactsState.Retry -> currentState
    }
  }
}

class Identity<T> : (T) -> T {
  override fun invoke(value: T): T {
    return value
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    return true
  }

  override fun hashCode(): Int {
    return javaClass.hashCode()
  }
}
