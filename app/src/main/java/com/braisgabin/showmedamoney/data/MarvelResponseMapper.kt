package com.braisgabin.showmedamoney.data

import com.braisgabin.showmedamoney.entities.Contact

class MarvelResponseMapper(
    val data: MarvelListMapper
) {
  fun toDomain(): List<Contact> {
    return data.results.map { Contact(it.id.toString(), it.name, it.thumbnail.path, null) }
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
