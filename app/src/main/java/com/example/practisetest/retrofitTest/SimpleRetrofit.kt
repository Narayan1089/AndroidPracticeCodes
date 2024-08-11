package com.example.practisetest.retrofitTest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practisetest.R
import com.example.practisetest.retrofitTest.models.CourseDataModal
import com.example.practisetest.retrofitTest.retrofits.RetrofitAPI
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SimpleRetrofit : AppCompatActivity() {
    lateinit var courseNameTV: TextView
    lateinit var courseDescTV: TextView
    lateinit var courseReqTV: TextView
    lateinit var courseIV: ImageView
    lateinit var visitCourseBtn: Button
    lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_retrofit)

        // on below line we are initializing our variable with their ids.
        courseNameTV = findViewById(R.id.idTVCourseName)
        courseDescTV = findViewById(R.id.idTVDesc)
        courseReqTV = findViewById(R.id.idTVPreq)
        courseIV = findViewById(R.id.idIVCourse)
        visitCourseBtn = findViewById(R.id.idBtnVisitCourse)
        loadingPB = findViewById(R.id.idLoadingPB)

        getData()

    }

    private fun getData() {
        val okHttpClient =
            OkHttpClient.Builder().hostnameVerifier { hostname, session -> true }.build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://jsonkeeper.com/b/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)

        val call: Call<CourseDataModal?>? = retrofitAPI.getCourse()

        call!!.enqueue(object : Callback<CourseDataModal?> {
            override fun onResponse(
                call: Call<CourseDataModal?>,
                response: Response<CourseDataModal?>
            ) {
                if (response.isSuccessful) {

                    loadingPB.visibility = View.GONE

                    val courseName: String = response.body()!!.courseName
                    val courseLink: String = response.body()!!.courseLink
                    val courseImg: String = response.body()!!.courseimg
                    val courseDesc: String = response.body()!!.courseDesc
                    val coursePreq: String = response.body()!!.Prerequisites

                    courseReqTV.text = coursePreq
                    courseDescTV.text = courseDesc
                    courseNameTV.text = courseName

                    Picasso.get().load(courseImg).into(courseIV)

                    visitCourseBtn.visibility = View.VISIBLE

                    visitCourseBtn.setOnClickListener {
                        val i = Intent(Intent.ACTION_VIEW)
                        i.setData(Uri.parse(courseLink))
                        startActivity(i)
                    }
                }
            }

            override fun onFailure(call: Call<CourseDataModal?>, t: Throwable) {
                Toast.makeText(this@SimpleRetrofit, "Fail to get the data..", Toast.LENGTH_SHORT)
                    .show()
                Log.e("Faillle","Failue $t")
            }
        })
    }
}