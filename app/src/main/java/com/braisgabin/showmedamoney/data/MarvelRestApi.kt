package com.braisgabin.showmedamoney.data

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface MarvelRestApi {

  @GET("v1/public/characters")
  fun characters(): Single<Response<MarvelResponseMapper>>
}
