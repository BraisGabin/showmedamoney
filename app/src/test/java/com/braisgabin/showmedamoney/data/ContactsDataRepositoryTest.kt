package com.braisgabin.showmedamoney.data

import arrow.core.Either
import com.braisgabin.showmedamoney.contact
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class ContactsDataRepositoryTest {

    private val apiSource: ContactsApiSource = mock {
        on { retrieveContacts() }.thenReturn(Single.just(Either.right(listOf(contact(id = "api")))))
    }
    private val deviceSource: ContactsDeviceSource = mock {
        on { retrieveContacts() }.thenReturn(Single.just(Either.right(listOf(contact(id = "device")))))
    }

    private val subject = ContactsDataRepository(apiSource, deviceSource)

    @Test
    fun retrieveContacts() {
        subject.retrieveContacts()
            .test()
            .assertValue(Either.right(listOf(contact(id = "api"), contact(id = "device"))))
    }

    @Test
    fun retrieveContacts_apiError() {
        val data = Either.left(Exception())
        whenever(apiSource.retrieveContacts()).thenReturn(Single.just(data))
        subject.retrieveContacts()
            .test()
            .assertValue(data)
    }

    @Test
    fun retrieveContacts_deviceError() {
        val data = Either.left(Exception())
        whenever(deviceSource.retrieveContacts()).thenReturn(Single.just(data))
        subject.retrieveContacts()
            .test()
            .assertValue(data)
    }

    @Test
    fun retrieveContacts_bothError() {
        val data = Either.left(Exception())
        val data1 = Either.left(RuntimeException())
        whenever(apiSource.retrieveContacts()).thenReturn(Single.just(data))
        whenever(deviceSource.retrieveContacts()).thenReturn(Single.just(data1))
        subject.retrieveContacts()
            .test()
            .assertValue(data)
    }
}
