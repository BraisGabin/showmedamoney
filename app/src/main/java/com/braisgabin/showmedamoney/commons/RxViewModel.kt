package com.braisgabin.showmedamoney.commons

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class RxViewModel: ViewModel() {
  val disposable = CompositeDisposable()

  override fun onCleared() {
    disposable.dispose()
  }
}
