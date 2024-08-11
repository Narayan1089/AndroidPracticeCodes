package com.example.practisetest.retrofitTest.retrofits

import com.example.practisetest.retrofitTest.models.CourseDataModal
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("8RFY")
    fun getCourse(): Call<CourseDataModal?>?
}