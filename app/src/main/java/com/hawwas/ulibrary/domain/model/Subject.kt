package com.hawwas.ulibrary.domain.model

data class Subject(
    val version: String,
    val name: String,
    var remotePath: String,
    val items: MutableList<Item>
) {
    var hidden=false
    var totalSize = 0L
}

