package com.hawwas.ulibrary.model

data class Item(
    val name: String,
    val author: String,
    val catalog: String,
    var version: String,
) {
    var stared: Boolean = false
    var lastWatched: Long = 0
    var filePath: String = ""
}
