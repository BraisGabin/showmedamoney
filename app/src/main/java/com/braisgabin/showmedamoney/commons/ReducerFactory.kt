package com.braisgabin.showmedamoney.commons

import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

@Mockable
class ReducerFactory {

  fun <T> create(initialState: T, partialStates: Flowable<(T) -> T>, action: (T) -> Unit): Disposable {
    return partialStates
        .scan(initialState) { currentState, partialState ->
          partialState(currentState)
        }
        .subscribe(
            { action(it) },
            { throw RuntimeException(it) },
            { throw IllegalStateException("The show should go on!") })
  }
}
