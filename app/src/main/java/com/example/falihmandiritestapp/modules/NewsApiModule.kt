package com.example.falihmandiritestapp.modules

import com.example.falihmandiritestapp.BuildConfig
import com.example.falihmandiritestapp.api.APIServices
import com.example.falihmandiritestapp.common.NEWS_API_BASE_URL
import com.example.falihmandiritestapp.common.NEWS_API_KEY
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIME_OUT: Long = 30

val newsApiModule = module {
    factory { provideHttpLoggingInterceptor() }
    factory { provideGson() }
    factory { provideOkHttpClient(get()) }
    factory { provideNewsApi(get()) }
    single { provideRetrofit(get(), get()) }
}


fun provideNewsApi(retrofit: Retrofit): APIServices = retrofit.create(APIServices::class.java)

fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
    return Retrofit.Builder()
        .baseUrl(NEWS_API_BASE_URL)
//        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

    val builder = OkHttpClient.Builder()

    builder
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader(
                "X-Api-Key", NEWS_API_KEY
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