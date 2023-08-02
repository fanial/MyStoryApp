package com.codefal.mystoryapp.database

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("database")
	val database: Database? = null,

	@field:SerializedName("formatVersion")
	val formatVersion: Int? = null
)

data class FieldsItem(

	@field:SerializedName("notNull")
	val notNull: Boolean? = null,

	@field:SerializedName("fieldPath")
	val fieldPath: String? = null,

	@field:SerializedName("columnName")
	val columnName: String? = null,

	@field:SerializedName("affinity")
	val affinity: String? = null
)

data class EntitiesItem(

	@field:SerializedName("indices")
	val indices: List<Any?>? = null,

	@field:SerializedName("foreignKeys")
	val foreignKeys: List<Any?>? = null,

	@field:SerializedName("fields")
	val fields: List<FieldsItem?>? = null,

	@field:SerializedName("tableName")
	val tableName: String? = null,

	@field:SerializedName("primaryKey")
	val primaryKey: PrimaryKey? = null
)

data class Database(

	@field:SerializedName("entities")
	val entities: List<EntitiesItem?>? = null,

	@field:SerializedName("version")
	val version: Int? = null
)

data class PrimaryKey(

	@field:SerializedName("columnNames")
	val columnNames: List<String?>? = null,

	@field:SerializedName("autoGenerate")
	val autoGenerate: Boolean? = null
)
