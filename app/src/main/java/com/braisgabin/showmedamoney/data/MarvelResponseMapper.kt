package com.braisgabin.showmedamoney.data

import com.braisgabin.showmedamoney.entities.Contact
import okhttp3.HttpUrl

class MarvelResponseMapper(
    val data: MarvelListMapper
) {
  fun toDomain(): List<Contact> {
    return data.results.map {
      val thumbnail = HttpUrl.parse(it.thumbnail.path + "/standard_xlarge.jpg")!!
          .newBuilder()
          .scheme("https")
          .build()
          .toString()
      Contact(it.id.toString(), it.name, thumbnail, null)
    }
  }
}

class MarvelListMapper(
    val results: List<MarvelContactMapper>
)

class MarvelContactMapper(
    val id: Long,
    val name: String,
    val thumbnail: MarvelThumbnailMapper
)

class MarvelThumbnailMapper(
    val path: String
)
