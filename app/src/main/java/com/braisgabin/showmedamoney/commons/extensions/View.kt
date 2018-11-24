package com.braisgabin.showmedamoney.commons.extensions

import android.view.View

fun List<View>.allGoneExcept(vararg except: View) {
  forEach { view ->
    view.visibility = if (except.any { it === view }) View.VISIBLE else View.GONE
  }
}
