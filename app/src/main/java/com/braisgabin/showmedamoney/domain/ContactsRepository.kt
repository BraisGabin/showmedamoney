package com.braisgabin.showmedamoney.domain

import arrow.core.Either
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single

interface ContactsRepository {
  fun retrieveContacts(): Single<Either<Throwable, List<Contact>>>
}
