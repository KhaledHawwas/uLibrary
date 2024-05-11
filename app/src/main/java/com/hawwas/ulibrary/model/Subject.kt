package com.hawwas.ulibrary.model

data class Subject(
    val version: String,
    val name: String,
    var filePath: String,
    val remotePath: String,
    val items : List<Item>
)

