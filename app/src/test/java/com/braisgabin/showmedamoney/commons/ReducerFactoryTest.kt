package com.braisgabin.showmedamoney.commons

import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.PublishSubject
import org.junit.Test

class ReducerFactoryTest {
  private val partialStates = PublishSubject.create<(String) -> String>()
  private val partialStatesFlowable = partialStates.toFlowable(BackpressureStrategy.BUFFER)

  private val subject = ReducerFactory()

  @Test
  fun `check initial value send`() {
    subject.create("1", partialStatesFlowable)
        .test()
        .assertValuesOnly("1")
  }

  @Test
  fun `Emit partial state calls action`() {
    val testObserver = subject.create("1", partialStatesFlowable)
        .test()
        .assertValuesOnly("1")


    partialStates.onNext({ a: String -> a + "2" })

    testObserver
        .assertValuesOnly("1", "12")

    partialStates.onNext({ a: String -> a + "3" })

    testObserver
        .assertValuesOnly("1", "12", "123")
  }
}
