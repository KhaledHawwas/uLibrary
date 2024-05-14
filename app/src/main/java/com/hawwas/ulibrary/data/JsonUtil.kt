package com.hawwas.ulibrary.data

import com.hawwas.ulibrary.model.*
import org.json.*

private const val TAG = "KH_JsonUtil"
fun toSubjectsInfoList(json: String): List<SubjectHeader> {
    val subjects = mutableListOf<SubjectHeader>()
    val jsonArray = JSONObject(json).getJSONArray("subjects")
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val name = jsonObject.getString("name")
        val path = jsonObject.getString("path")
        subjects.add(SubjectHeader(name, path))
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
        val jsonItem = items.getJSONObject(i)
        val item = Item(
            jsonItem.getString("name"),
            jsonItem.getString("author"),
            jsonItem.getString("catalog"),
            jsonItem.getString("version"),
            jsonItem.getString("remote_path")
        )
        if (!jsonItem.has("file_path"))
            item.filePath = name + "/" + item.getCatalogDir() + item.name
        else
            item.filePath = jsonItem.getString("file_path")

        itemsList.add(item)
    }
    return Subject(version, name, remotePath, itemsList)
}

