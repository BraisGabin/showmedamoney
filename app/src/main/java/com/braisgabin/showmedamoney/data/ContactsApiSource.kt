package com.braisgabin.showmedamoney.data

import arrow.core.Either
import com.braisgabin.showmedamoney.commons.Mockable
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single
import javax.inject.Inject

@Mockable
class ContactsApiSource @Inject constructor(
    private val restApi: MarvelRestApi
) {
  fun retrieveContacts(): Single<Either<Throwable, List<Contact>>> {
    return restApi.characters()
        .map { response ->
          if (response.isSuccessful) {
            Either.right(response.body()!!.toDomain())
          } else {
            Either.left(IllegalStateException() as Throwable)
          }
        }
        .onErrorReturn {
          Either.left(it)
        }
  }
}
