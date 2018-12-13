package com.braisgabin.showmedamoney.di

import android.content.ContentResolver
import android.content.Context
import com.braisgabin.showmedamoney.data.MarvelInterceptor
import com.braisgabin.showmedamoney.data.MarvelRestApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class DataModule {

  @Module
  companion object {

    @JvmStatic
    @Provides
    fun contentResolverProvider(context: Context): ContentResolver {
      return context.contentResolver
    }

    @JvmStatic
    @Provides
    @Reusable
    fun gsonProvider(): Gson {
      return GsonBuilder()
          .create()
    }

    @JvmStatic
    @Provides
    fun httpLoggingInterceptorProvider(): HttpLoggingInterceptor {
      val httpLoggingInterceptor = HttpLoggingInterceptor()
      httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
      return httpLoggingInterceptor
    }

    @JvmStatic
    @Provides
    @Singleton
    fun okHttpClientProvider(
        marvelInterceptor: MarvelInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
      return OkHttpClient().newBuilder()
          .addNetworkInterceptor(marvelInterceptor)
          .addNetworkInterceptor(httpLoggingInterceptor)
          .build()
    }

    @JvmStatic
    @Provides
    @Reusable
    fun retrofitProvider(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
      return Retrofit.Builder()
          .baseUrl("https://gateway.marvel.com/")
          .addConverterFactory(GsonConverterFactory.create(gson))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
          .callFactory { okHttpClient.newCall(it) }
          .build()
    }

    @JvmStatic
    @Provides
    fun marvelRestApiProvider(retrofit: Retrofit): MarvelRestApi {
      return retrofit.create(MarvelRestApi::class.java)
    }
  }
}
