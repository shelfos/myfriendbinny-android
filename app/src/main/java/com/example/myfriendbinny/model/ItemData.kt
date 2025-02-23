package com.example.myfriendbinny.model

import kotlinx.serialization.Serializable

@Serializable
data class ItemData(
    val id: String,
    var binId: String,
    val name: String,
    val description: String
) {
    //specialized isEmpty() function when all fields are blank
    fun isEmpty(): Boolean {
        return id.isBlank() && binId.isBlank() && name.isBlank() && description.isBlank()
    }
}
