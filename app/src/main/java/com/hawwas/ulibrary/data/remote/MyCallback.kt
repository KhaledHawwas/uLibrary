package com.hawwas.ulibrary.data.remote

data class MyCallback(
    val success: (String) -> Unit,
    val failure: (Throwable) -> Unit =
        { e -> e.printStackTrace() }//TODO
)
