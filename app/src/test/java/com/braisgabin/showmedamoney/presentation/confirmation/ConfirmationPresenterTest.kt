package com.braisgabin.showmedamoney.presentation.confirmation

import com.braisgabin.showmedamoney.contact
import org.junit.Test
import java.math.BigDecimal

class ConfirmationPresenterTest {

    @Test
    fun `test single contact`() {
        ConfirmationPresenter(BigDecimal("1"), listOf(contact("1")))
            .states
            .test()
            .assertValue(ConfirmationState(
                BigDecimal("1"),
                listOf(
                    Split(BigDecimal("1.00"), contact("1"))
                )))
    }

    @Test
    fun `test two contacts`() {
        ConfirmationPresenter(BigDecimal("1"), listOf(contact("1"), contact("2")))
            .states
            .test()
            .assertValue(ConfirmationState(
                BigDecimal("1"),
                listOf(
                    Split(BigDecimal("0.50"), contact("1")),
                    Split(BigDecimal("0.50"), contact("2"))
                )))
    }

    @Test
    fun `test three contacts with problems with rounding`() {
        ConfirmationPresenter(BigDecimal("1"), listOf(contact("1"), contact("2"), contact("3")))
            .states
            .test()
            .assertValue(ConfirmationState(
                BigDecimal("1"),
                listOf(
                    Split(BigDecimal("0.33"), contact("1")),
                    Split(BigDecimal("0.33"), contact("2")),
                    Split(BigDecimal("0.34"), contact("3"))
                )))
    }
}
