package com.valllent.giphy.data

data class User(
    val name: String,
    val country: String,
    val age: Int,
    val id: Int = lastId++
) {
    companion object {
        private var lastId = 0
    }
}