package com.braisgabin.showmedamoney.data

import com.braisgabin.showmedamoney.commons.extensions.md5
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MarvelInterceptor @Inject constructor() : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val currentTime = System.currentTimeMillis()

    val url = chain.request().url
        .newBuilder()
        .addQueryParameter("ts", "$currentTime")
        .addQueryParameter("apikey", publicKey)
        .addQueryParameter("hash", "$currentTime$privateKey$publicKey".md5().toLowerCase())
        .build()

    val request = chain.request().newBuilder()
        .url(url)
        .build()

    return chain.proceed(request)
  }
}

private const val publicKey = "87cb9a2ea3dc10a8cc83358b337339c5"
/*
 * I should move this outside the repository. Probably in a local gradle property.
 * But I really don't care about my Marvel dev account so... YOLO
 */
private const val privateKey = "dbb5864ef9da6067af56fc4558647d1522a08706"
