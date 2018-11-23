package com.braisgabin.showmedamoney.commons

open class OnlyOnce {
  private var called = false

  fun checkOnlyOnce() {
    if (called) {
      throw IllegalStateException("You call this twice!")
    } else {
      called = true
    }
  }
}
