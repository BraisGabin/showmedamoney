package com.braisgabin.showmedamoney.presentation.amount

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

// Extracted from https://stackoverflow.com/a/13716269/842697
class AmountFilter(
    private val maxAmount: Long,
    private val decimalSeparator: String
) : InputFilter {
  private val mPattern = Pattern.compile("(0|[1-9]+[0-9]*)?(\\$decimalSeparator[0-9]{0,2})?")

  override fun filter(source: CharSequence, start: Int, end: Int,
                      dest: Spanned, dstart: Int, dend: Int): CharSequence? {
    val result = dest.subSequence(0, dstart).toString() + source.toString() + dest.subSequence(dend, dest.length)

    if (result.isEmpty()) {
      return null
    }

    val matcher = mPattern.matcher(result)

    return if (!matcher.matches()) {
      dest.subSequence(dstart, dend)
    } else {
      val value: Long = matcher.group(1)?.let {
        if (it.isEmpty()) 0L else it.toLong()
      } ?: 0L
      when {
        value < maxAmount -> null
        value > maxAmount -> dest.subSequence(dstart, dend)
        else -> {
          val decimals = matcher.group(2)?.replace(decimalSeparator, "")
          if (decimals.isNullOrEmpty() || decimals.toInt() == 0) {
            null
          } else {
            dest.subSequence(dstart, dend)
          }
        }
      }
    }
  }
}

