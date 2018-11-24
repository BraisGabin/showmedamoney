package com.braisgabin.showmedamoney.presentation

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.android21buttons.fragmenttestrule.FragmentTestRule
import com.braisgabin.showmedamoney.R
import com.braisgabin.showmedamoney.contact
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ContactsFragmentTest {

  @Rule
  @JvmField
  val fragmentRule = FragmentTestRule.create(ContactsFragment::class.java)!!

  private val fragment
    get() = fragmentRule.fragment

  private fun runOnUiThread(action: () -> Unit) {
    fragmentRule.runOnUiThread(action)
  }

  private lateinit var testEvents: TestObserver<ContactsEvent>

  @Before
  fun setUp() {
    testEvents = fragment.events.test()
  }

  @Test
  fun showProgress() {
    runOnUiThread {
      fragment.render(ContactsState.Progress)
    }

    onView(withId(R.id.progressView)).check(matches(isDisplayed()))
    onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.retryView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.fab)).check(matches(not(isDisplayed())))
  }

  @Test
  fun showRetry() {
    runOnUiThread {
      fragment.render(ContactsState.Retry)
    }

    onView(withId(R.id.progressView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.retryView)).check(matches(isDisplayed()))
    onView(withId(R.id.fab)).check(matches(not(isDisplayed())))
  }

  @Test
  fun clickRetry() {
    runOnUiThread {
      fragment.render(ContactsState.Retry)
    }

    onView(withId(R.id.retryView))
        .perform(click())

    testEvents.assertValue(ContactsEvent.Retry)
  }

  @Test
  fun showDataNoNext() {
    runOnUiThread {
      fragment.render(ContactsState.Data(
          listOf(
              SelectedContact(contact(id = "1", name = "Spider Man"), false)),
          false))
    }

    onView(withId(R.id.progressView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    onView(withId(R.id.retryView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.fab)).check(matches(not(isDisplayed())))
  }

  @Test
  fun clickData() {
    runOnUiThread {
      fragment.render(ContactsState.Data(
          listOf(
              SelectedContact(contact(id = "1", name = "Spider Man"), false),
              SelectedContact(contact(id = "2", name = "Thor", phoneNumber = "phore"), true)),
          true))
    }

    onView(withText("Spider Man"))
        .perform(click())

    testEvents.assertValue(ContactsEvent.ClickContact(SelectedContact(contact(id = "1", name = "Spider Man"), false)))
  }

  @Test
  fun showDataNext() {
    runOnUiThread {
      fragment.render(ContactsState.Data(
          listOf(
              SelectedContact(contact(id = "1", name = "Spider Man"), true)),
          true))
    }

    onView(withId(R.id.progressView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    onView(withId(R.id.retryView)).check(matches(not(isDisplayed())))
    onView(withId(R.id.fab)).check(matches(isDisplayed()))
  }

  @Test
  fun clickNext() {
    runOnUiThread {
      fragment.render(ContactsState.Data(
          listOf(
              SelectedContact(contact(id = "1", name = "Spider Man"), true)),
          true))
    }

    onView(withId(R.id.fab))
        .perform(click())

    testEvents.assertValue(ContactsEvent.NextStepClick)
  }
}
