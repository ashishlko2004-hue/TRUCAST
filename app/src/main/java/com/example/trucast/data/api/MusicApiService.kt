package com.example.trucast.data.api

import com.example.trucast.data.model.ItunesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String = "bollywood",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 10
    ): ItunesSearchResponse
}
