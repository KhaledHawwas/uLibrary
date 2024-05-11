package com.hawwas.ulibrary.model
/**
 * This class is to hold the information of the subject so user can choose from it
 * @param name the name of the subject
 * @param remotePath the version of the subject
 */
data class SubjectInfo(
    val name: String,
    val remotePath: String,
    ){
    var selected: Boolean = false
}