package com.hawwas.ulibrary.data

import com.hawwas.ulibrary.domain.model.*

import org.json.*

private const val TAG = "KH_JsonUtil"
fun toSubjectsHeaderList(json: String): List<SubjectHeader> {
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

    val hidden = if (jsonObject.has("hidden")) jsonObject.getBoolean("hidden") else false
    val items = jsonObject.getJSONArray("items")
    val itemsList = mutableListOf<Item>()
    for (i in 0 until items.length()) {
        val jsonItem = items.getJSONObject(i)
        val item = Item(
            jsonItem.getString("name"),
            jsonItem.getString("author"),
            jsonItem.getString("category"),
            jsonItem.getString("version"),
            jsonItem.getString("remote_path"),
            name
        )
        if (jsonItem.has("last_watched")) item.lastWatched = jsonItem.getLong("last_watched")
        if (jsonItem.has("starred")) item.starred = jsonItem.getBoolean("starred")


        itemsList.add(item)
    }
    return Subject(version, name, remotePath, itemsList).apply { this.hidden = hidden }
}

fun subjectToJson(subject: Subject): String {
    val jsonObject = JSONObject()
    jsonObject.put("version", subject.version)
    jsonObject.put("name", subject.name)
    if (jsonObject.has("hidden")) jsonObject.put("hidden", subject.hidden)
    jsonObject.put("remote_path", subject.remotePath)
    val items = JSONArray()
    subject.items.forEach {
        val jsonItem = JSONObject()
        jsonItem.put("name", it.name)
        jsonItem.put("author", it.author)
        jsonItem.put("category", it.category)
        jsonItem.put("version", it.version)
        jsonItem.put("remote_path", it.remotePath)
        jsonItem.put("last_watched", it.lastWatched)
        jsonItem.put("starred", it.starred)
        items.put(jsonItem)
    }
    jsonObject.put("items", items)
    return jsonObject.toString()
}
