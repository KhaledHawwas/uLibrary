package com.hawwas.ulibrary.data

import com.google.gson.Gson
import com.hawwas.ulibrary.model.*
import org.json.*

fun toSubjectsInfoList(json: String): List<SubjectInfo> {
    val subjects = mutableListOf<SubjectInfo>()
    val jsonArray = JSONObject(json).getJSONArray("subjects")
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val name = jsonObject.getString("name")
        val path = jsonObject.getString("path")
        subjects.add(SubjectInfo( name,path))
    }
    return subjects
}

fun toSubject(json: String): Subject {

    val jsonObject = JSONObject(json)
    val version = jsonObject.getString("version")
    val name = jsonObject.getString("name")
    val remotePath = jsonObject.getString("remote_path")
    val items = jsonObject.getJSONArray("items")
    val itemsList = mutableListOf<Item>()
    for (i in 0 until items.length()) {
        val item = items.getJSONObject(i)
        itemsList.add(Item(
            item.getString("name"),
            item.getString("author"),
            item.getString("catalog"),
            item.getString("version")
        ))
    }
    return Subject(version,name,"",remotePath,itemsList)
}

