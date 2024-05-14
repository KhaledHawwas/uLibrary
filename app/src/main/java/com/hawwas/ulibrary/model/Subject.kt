package com.hawwas.ulibrary.model

data class Subject(
    val version: String,
    val name: String,
    val remotePath: String,
    val items: List<Item>
) {
    var totalSize = 0L
}

