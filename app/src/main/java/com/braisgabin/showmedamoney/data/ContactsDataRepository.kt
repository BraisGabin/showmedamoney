package com.braisgabin.showmedamoney.data

import arrow.core.Either
import com.braisgabin.showmedamoney.domain.ContactsRepository
import com.braisgabin.showmedamoney.entities.Contact
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ContactsDataRepository @Inject constructor(
    private val apiSource: ContactsApiSource,
    private val deviceSource: ContactsDeviceSource
) : ContactsRepository {
  override fun retrieveContacts(): Single<Either<Throwable, List<Contact>>> {
    return Single.zip(
        apiSource.retrieveContacts(),
        deviceSource.retrieveContacts(),
        BiFunction { apiResponse, deviceResponse ->
          apiResponse.fold( // I need to learn more functional... there must be an easier way
              { apiResponse },
              { apiContacts ->
                deviceResponse.fold(
                    { deviceResponse },
                    { deviceContacts ->
                      Either.right(apiContacts.plus(deviceContacts))
                    }
                )
              }
          )
        })
  }
}
