package com.example.falihmandiritestapp.modules

import android.content.Context
import com.example.falihmandiritestapp.BuildConfig
import com.example.falihmandiritestapp.R
import com.example.falihmandiritestapp.api.APIServices
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIME_OUT: Long = 30

val newsApiModule = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideGson() }
    factory { provideOkHttpClient(get(), androidContext()) }
    factory { provideNewsApi(get()) }
    single { provideRetrofit(get(), get(), androidContext()) }
}


fun provideNewsApi(retrofit: Retrofit): APIServices = retrofit.create(APIServices::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson, context: Context): Retrofit {
    return Retrofit.Builder()
        .baseUrl( context.getString(R.string.news_api_base_url))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

fun provideGson(): Gson {
    return GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setLenient()
        .create()
}

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor, context: Context): OkHttpClient {

    val builder = OkHttpClient.Builder()

    builder
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader(
                "X-Api-Key", context.getString(R.string.news_api_key)
            )
            chain.proceed(requestBuilder.build())
        }

    if (BuildConfig.DEBUG) {
        builder.addInterceptor(httpLoggingInterceptor)
    }

    return builder.build()
}

fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}