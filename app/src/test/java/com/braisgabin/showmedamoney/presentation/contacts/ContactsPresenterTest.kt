package com.braisgabin.showmedamoney.presentation.contacts

import arrow.core.Either
import com.braisgabin.showmedamoney.commons.ReducerFactory
import com.braisgabin.showmedamoney.contact
import com.braisgabin.showmedamoney.domain.GetContactsUseCase
import com.braisgabin.showmedamoney.entities.Contact
import com.braisgabin.showmedamoney.presentation.Navigator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import io.reactivex.subscribers.TestSubscriber
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class ContactsPresenterTest {

  private val contacts: Subject<Either<Throwable, List<Contact>>> = PublishSubject.create()
  private val render: (ContactsState) -> Unit = mock()
  private val reducerFactory: ReducerFactory = mock {
    on { create<Any>(any(), any()) } doReturn Flowable.never()
  }
  private val getContactsUseCase: GetContactsUseCase = mock {
    on { retrieveContacts() } doReturn contacts.take(1).singleOrError()
  }
  private val navigator: Navigator = mock()

  private val subject = ContactsPresenter(
      reducerFactory,
      getContactsUseCase,
      navigator
  )

  private lateinit var testSubscriber: TestSubscriber<(ContactsState) -> ContactsState>

  @Before
  fun setUp() {
    subject.states

    val argumentCaptor = argumentCaptor<Flowable<(ContactsState) -> ContactsState>>()
    verify(reducerFactory).create(eq(ContactsState.Progress), argumentCaptor.capture())
    verify(getContactsUseCase).retrieveContacts()

    testSubscriber = argumentCaptor.firstValue.test()
  }

  @After
  fun tearDown() {
    verifyNoMoreInteractions(render, reducerFactory, getContactsUseCase, navigator)
  }

  @Test
  fun `correct call to reducerFactory1`() {
    val data = Either.right(emptyList<Contact>())
    contacts.onNext(data)

    testSubscriber
        .assertValue(RetrieveDataPartialState(data))
  }

  @Test
  fun `correct call to reducerFactory2`() {
    val data = Either.right(emptyList<Contact>())
    contacts.onNext(data)

    subject.events.accept(ContactsEvent.Retry)
    verify(getContactsUseCase, times(2)).retrieveContacts()

    val data2 = Either.left(Exception())
    contacts.onNext(data2)

    testSubscriber
        .assertValues(RetrieveDataPartialState(data), RetrieveDataPartialState(data2))
  }

  @Test
  fun `correct call to reducerFactory3`() {
    subject.events.accept(ContactsEvent.ClickContact(SelectedContact(contact("1"), false)))

    testSubscriber
        .assertValue(ClickContactPartialState(SelectedContact(contact("1"), false)))
  }

  @Test
  fun `correct call to reducerFactory4`() {
    subject.events.accept(ContactsEvent.NextStepClick)

    testSubscriber
        .assertValue(Identity())
    verify(navigator).step2(emptyList())
  }

  @Test
  fun `the presenter always returns the same flowable`() {
    assertThat(subject.states, `is`(subject.states))
  }
}
