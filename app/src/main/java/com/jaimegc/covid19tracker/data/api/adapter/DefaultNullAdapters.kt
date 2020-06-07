package com.jaimegc.covid19tracker.data.api.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

/**
 *  For this example: data class Foo(@Json(name = "dates") val bar: Long)
 *  These adapters work in this case: { "bar": null }
 *  but not in this: { }
 *  In Gson, in both cases we get the value 0L. With Moshi, we need to add Long? to avoid using
 *  these objects because in our model, all fields can be null or empty. If they were only null,
 *  we can use Long (with no ?) and these adapters.
 */
object NullToEmptyDoubleAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Double {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextDouble()
        }
        reader.nextNull<Unit>()

        return 0.0
    }
}

object NullToEmptyLongAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Long {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextLong()
        }
        reader.nextNull<Unit>()

        return 0L
    }
}

object NullToEmptyFloatAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Double {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextDouble()
        }
        reader.nextNull<Unit>()

        return 0.0
    }
}

object NullToEmptyIntAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Int {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextInt()
        }
        reader.nextNull<Unit>()

        return 0
    }
}

object NullToEmptyStringAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): String {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextString()
        }
        reader.nextNull<Unit>()

        return ""
    }
}

object NullToEmptyItemAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): Double {
        if (reader.peek() != JsonReader.Token.NULL) {
            return reader.nextDouble()
        }
        reader.nextNull<Unit>()

        return 0.0
    }
}