package com.example.test.Fragment.Photo.Loader

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PhotoApi {
    // https://api.unsplash.com/photos/random?client_id=a71207e3004d068f560b627addb033c6a48387b990e63175e38d1af00f79c355&count=1

    @GET("photos/random?client_id=$access_token")
    fun search(@Query("count") count: Int): Observable<List<RawPhoto>>

    @GET ("photos/{id}/?client_id=$access_token")
    fun single(@Path(value = "id",  encoded = true) id: String): Observable<RawPhoto>


    companion object Factory {
        const val access_token = "a71207e3004d068f560b627addb033c6a48387b990e63175e38d1af00f79c355"

        fun create(): PhotoApi {

            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(PhotoDeserializer.gson))
                    .baseUrl("https://api.unsplash.com/")
                    .build()

            return retrofit.create(PhotoApi::class.java)
        }
    }
}