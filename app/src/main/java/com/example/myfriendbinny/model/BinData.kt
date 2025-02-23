package com.example.myfriendbinny.model

import kotlinx.serialization.Serializable

@Serializable
data class BinData(
    val id: String,
    val name: String,
    val description: String
) {
    //specialized isEmpty() function when all fields are blank
    fun isEmpty(): Boolean {
        return id.isBlank() && name.isBlank() && description.isBlank()
    }
}
