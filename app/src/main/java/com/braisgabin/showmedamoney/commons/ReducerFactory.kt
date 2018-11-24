package com.braisgabin.showmedamoney.commons

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@Mockable
class ReducerFactory @Inject constructor(private val lifecycle: Lifecycle) {

  fun <T> create(initialState: T, partialStates: Flowable<(T) -> T>, action: (T) -> Unit): Disposable {
    val states = partialStates
        .scan(initialState) { currentState, partialState ->
          partialState(currentState)
        }
        .replay(1)

    LiveDataReactiveStreams.fromPublisher(states)
        .observe({ lifecycle }, { action(it!!) })

    return states.connect()
  }
}
