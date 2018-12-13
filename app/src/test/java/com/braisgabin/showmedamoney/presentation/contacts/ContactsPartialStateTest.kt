package com.braisgabin.showmedamoney.presentation.contacts

import arrow.core.Either
import com.braisgabin.showmedamoney.contact
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class RetrieveDataPartialStateTest {
  private var subject = RetrieveDataPartialState(Either.right(listOf(contact(id = "1"), contact(id = "2"))))

  @Test
  fun `retrieve data with progress`() {
    assertThat(subject(ContactsState.Progress), `is`(ContactsState.Data(listOf(
        SelectedContact(contact("1"), false),
        SelectedContact(contact("2"), false)), false) as ContactsState))
  }

  @Test
  fun `retrieve data with retry`() {
    assertThat(subject(ContactsState.Retry), `is`(ContactsState.Data(listOf(
        SelectedContact(contact("1"), false),
        SelectedContact(contact("2"), false)), false) as ContactsState))
  }

  @Test
  fun `retrieve data with data`() {
    assertThat(subject(ContactsState.Data(emptyList(), false)), `is`(ContactsState.Data(listOf(
        SelectedContact(contact("1"), false),
        SelectedContact(contact("2"), false)), false) as ContactsState))
  }

  @Test
  fun `retrieve error with progress`() {
    subject = RetrieveDataPartialState(Either.left(Exception()))
    assertThat(subject(ContactsState.Progress), `is`(ContactsState.Retry as ContactsState))
  }

  @Test
  fun `retrieve error with retry`() {
    subject = RetrieveDataPartialState(Either.left(Exception()))
    assertThat(subject(ContactsState.Retry), `is`(ContactsState.Retry as ContactsState))
  }

  @Test
  fun `retrieve error with data`() {
    subject = RetrieveDataPartialState(Either.left(Exception()))
    assertThat(subject(ContactsState.Data(emptyList(), false)), `is`(ContactsState.Retry as ContactsState))
  }
}

class ClickContactPartialStateTest {
  private var subject = ClickContactPartialState(SelectedContact(contact(id = "1"), false))

  @Test
  fun `if we call with Progress we get the same`() {
    assertThat(subject(ContactsState.Progress), `is`(ContactsState.Progress as ContactsState))
  }

  @Test
  fun `if we call with Retry we get the same`() {
    assertThat(subject(ContactsState.Retry), `is`(ContactsState.Retry as ContactsState))
  }

  @Test
  fun `if we call with Data the selection changes positive`() {
    val data = listOf(
        SelectedContact(contact("1"), false),
        SelectedContact(contact("2"), false))
    val data2 = listOf(
        SelectedContact(contact("1"), true),
        SelectedContact(contact("2"), false))
    assertThat(subject(ContactsState.Data(data, false)), `is`(ContactsState.Data(data2, true) as ContactsState))
  }

  @Test
  fun `if we call with Data the selection changes negative`() {
    subject = ClickContactPartialState(SelectedContact(contact(id = "1"), true))
    val data = listOf(
        SelectedContact(contact("1"), true),
        SelectedContact(contact("2"), false))
    val data2 = listOf(
        SelectedContact(contact("1"), false),
        SelectedContact(contact("2"), false))
    assertThat(subject(ContactsState.Data(data, true)), `is`(ContactsState.Data(data2, false) as ContactsState))
  }

  @Test
  fun `if we call with Data the selection changes at least one selected`() {
    subject = ClickContactPartialState(SelectedContact(contact(id = "1"), true))
    val data = listOf(
        SelectedContact(contact("1"), true),
        SelectedContact(contact("2"), true))
    val data2 = listOf(
        SelectedContact(contact("1"), false),
        SelectedContact(contact("2"), true))
    assertThat(subject(ContactsState.Data(data, true)), `is`(ContactsState.Data(data2, true) as ContactsState))
  }
}

class IdentityTest {
  private val subject = Identity<String>()

  @Test
  fun `assert identity`() {
    assertThat(subject("1"), `is`("1"))
    assertThat(subject("2"), `is`("2"))
  }
}

