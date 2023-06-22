package com.codefal.mystoryapp.network.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseStories(

    @field:SerializedName("listStory")
	val listStory: List<ListStoryItem>? = null,

    @field:SerializedName("error")
	val error: Boolean? = null,

    @field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
@Entity(tableName = "table_story")
data class ListStoryItem(

	@PrimaryKey
	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("lon")
	val lon: Double? = null,

	@field:SerializedName("lat")
	val lat: Double? = null
) : Parcelable
/*

fun Story.toStoryUser() = Story(
	id = this.id?: "Data is null",
	name = this.name?: "Data is null",
	createdAt = this.createdAt?: "Data is null",
	photoUrl = this.photoUrl?: "Data is null",
	description = this.description?: "Data is null",
	lat = this.lat?: 0.0,
	lon = this.lon?: 0.0
)


fun List<Story>.toGenereteListStory(): MutableList<Story>{
	val listStoryUser = mutableListOf<Story>()
	this.forEach{listStoryUser.add(it.toStoryUser())}
	return listStoryUser
}
*/
