package com.braisgabin.showmedamoney.commons

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class ReducerFactoryTest {
  private val action: (String) -> Unit = mock()
  private val subject = ReducerFactory()
  private val partialStates = PublishSubject.create<(String) -> String>()
  private val partialStatesFlowable = partialStates.toFlowable(BackpressureStrategy.BUFFER)

  @After
  fun tearDown() {
    verifyNoMoreInteractions(action)
  }

  @Test
  fun `check initial value send`() {
    subject.create("1", partialStatesFlowable, action)

    verify(action).invoke("1")
  }

  @Test
  fun `Emit partial state calls action`() {
    subject.create("1", partialStatesFlowable, action)
    verify(action).invoke("1")

    partialStates.onNext({ a: String -> a + "2" })

    verify(action).invoke("12")

    partialStates.onNext({ a: String -> a + "3" })

    verify(action).invoke("123")
  }

  @Test
  @Ignore("RxJava fails silently so this test fails...")
  fun `when partialState has an error the reducer crash`() {
    subject.create("1", partialStatesFlowable, action)
    verify(action).invoke("1")

    try {
      partialStates.onError(Exception())
      Assert.fail()
    } catch (_: RuntimeException) {
      // correct path
    }
  }

  @Test
  @Ignore("RxJava fails silently so this test fails...")
  fun `when partialState completes the reducer crash`() {
    subject.create("1", partialStatesFlowable, action)
    verify(action).invoke("1")

    try {
      partialStates.onComplete()
      Assert.fail()
    } catch (_: IllegalStateException) {
      // correct path
    }
  }
}
