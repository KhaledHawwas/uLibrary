package com.hawwas.ulibrary.model

data class Subject(
    val version: String,
    val name: String,
    val remotePath: String,
    val items: MutableList<Item>
) {
    var hidden=false
    var totalSize = 0L
}

