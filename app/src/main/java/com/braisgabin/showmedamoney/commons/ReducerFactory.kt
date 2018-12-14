package com.braisgabin.showmedamoney.commons

import io.reactivex.Flowable
import javax.inject.Inject

@Mockable
class ReducerFactory @Inject constructor() {

  fun <T> create(initialState: T, partialStates: Flowable<(T) -> T>): Flowable<T> {
    return partialStates
        .scan(initialState) { currentState, partialState ->
          partialState(currentState)
        }
  }
}
