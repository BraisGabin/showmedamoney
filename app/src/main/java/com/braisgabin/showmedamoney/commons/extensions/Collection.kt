package com.braisgabin.showmedamoney.commons.extensions

import java.util.ArrayList

fun <E> Collection<E>.toArrayList(): ArrayList<E> {
  return ArrayList(this)
}
