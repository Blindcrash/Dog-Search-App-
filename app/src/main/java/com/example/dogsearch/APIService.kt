package com.example.dogsearch

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    fun getDogsByBreed(@Url url:String):Response<DogResponse>
}