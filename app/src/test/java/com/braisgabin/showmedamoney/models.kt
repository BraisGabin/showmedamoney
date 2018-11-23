package com.braisgabin.showmedamoney

import com.braisgabin.showmedamoney.entities.Contact

fun contact(
    id: String = "id",
    name: String = "name",
    imageUrl: String? = null,
    phoneNumber: String? = null
) = Contact(id, name, imageUrl, phoneNumber)
