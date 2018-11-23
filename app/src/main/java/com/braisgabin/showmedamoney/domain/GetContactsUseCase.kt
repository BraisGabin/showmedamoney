package com.braisgabin.showmedamoney.domain

import arrow.core.Either
import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single

@Mockable
class GetContactsUseCase {
  fun retrieveContacts(): Single<Either<Throwable, List<Contact>>> {
    TODO("Working on it")
  }
}
