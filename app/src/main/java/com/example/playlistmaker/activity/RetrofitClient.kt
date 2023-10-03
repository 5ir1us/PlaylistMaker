package com.example.playlistmaker.activity

import com.example.playlistmaker.Interface.RetrofitServices
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

  val errorInterceptor = Interceptor { chain ->
    // получаем оригинальный запрос
    val originalRequest = chain.request()
    // продолжаем цепочку с оригинальным запросом и получаем ответ
    val response = chain.proceed(originalRequest)
    // проверяем код ответа
    if (response.code == 200) {
      // изменяем код ответа на 400
      response.newBuilder()
        .code(400)
        .build()
    } else {
      // возвращаем оригинальный ответ
      response
    }
  }

  val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
      setLevel(HttpLoggingInterceptor.Level.BODY)
    })
    // .addInterceptor(errorInterceptor) //код 400 проверка
    .build()

  private var retrofit: Retrofit? = null
  private fun getClient(baseUrl: String): Retrofit {
    if (retrofit == null) retrofit = Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(client)  // только один будет работать клиент
      .addConverterFactory(GsonConverterFactory.create())
      .build()
    return retrofit!!
  }

  private val BASE_URL = "https://itunes.apple.com"
  val retrofitService: RetrofitServices
    get() = getClient(BASE_URL).create(RetrofitServices::class.java)
}