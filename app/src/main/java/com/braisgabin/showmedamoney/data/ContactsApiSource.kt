package com.braisgabin.showmedamoney.data

import arrow.core.Either
import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single
import javax.inject.Inject

@Mockable
class ContactsApiSource @Inject constructor() {
  fun retrieveContacts(): Single<Either<Throwable, List<Contact>>> {
    TODO()
  }
}
