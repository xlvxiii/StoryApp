package com.example.storyapp

import com.example.storyapp.data.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "name $i",
                description = "description $i",
                photoUrl = "photoUrl $i",
                createdAt = "createdAt $i",
                lat = i.toDouble(),
                lon = i.toDouble()
            )
            items.add(story)
        }
        return items
    }
}