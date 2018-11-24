package com.braisgabin.showmedamoney.domain

import arrow.core.Either
import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single
import javax.inject.Inject

@Mockable
class GetContactsUseCase @Inject constructor() {
  fun retrieveContacts(): Single<Either<Throwable, List<Contact>>> {
    return Single.just(Either.right(emptyList()))
  }
}
