package com.codefal.mystoryapp

import com.codefal.mystoryapp.network.model.ListStoryItem

object DataDummy {

    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/1745px-Android_robot.svg.png",
                "Today $i",
                "Testing $i",
                "Testing View Model $i",
                0.0,
                0.0,
            )
            items.add(story)
        }
        return items
    }
}