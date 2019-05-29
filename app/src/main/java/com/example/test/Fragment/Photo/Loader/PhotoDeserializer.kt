package com.example.test.Fragment.Photo.Loader

import com.google.gson.*
import java.lang.reflect.Type

internal class PhotoDeserializer : JsonDeserializer<RawPhoto> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): RawPhoto {
        val links: RawPhoto.Urls
        val data = json.asJsonObject

        val photoLinks = gson.fromJson(data.get("urls"), RawPhoto.Urls::class.java)
        links = photoLinks

        return RawPhoto(data.get("id").asString, links, data.get("width").asInt, data.get("height").asInt)
    }

    companion object {
        val gson = GsonBuilder()
                .registerTypeAdapter(RawPhoto::class.java, PhotoDeserializer())
                .create()
    }
}