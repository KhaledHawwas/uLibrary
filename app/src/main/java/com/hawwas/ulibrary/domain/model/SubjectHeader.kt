package com.hawwas.ulibrary.domain.model

/**
 * This class is to hold the information of the subject so user can choose from it
 * @param name the name of the subject
 * @param remotePath the version of the subject
 */
data class SubjectHeader(
    val name: String,
    val remotePath: String,
) {
    var selected: Boolean = false
}