package com.example.falihmandiritestapp.di.module

import com.example.falihmandiritestapp.BuildConfig
import com.example.falihmandiritestapp.api.APIServices
import com.example.falihmandiritestapp.common.API_TIMEOUT
import com.example.falihmandiritestapp.common.NEWS_API_BASE_URL
import com.example.falihmandiritestapp.common.NEWS_API_KEY
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiGeneratorModule {

	@Provides
	@Singleton
	internal fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
		val httpClient = OkHttpClient.Builder()
		httpClient.addInterceptor { chain ->
			val requestBuilder = chain.request().newBuilder()
			requestBuilder.addHeader(
				"X-Api-Key", NEWS_API_KEY
			)
			chain.proceed(requestBuilder.build())
		}
			.connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
			.readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
			.writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)

		if (BuildConfig.DEBUG) {
			httpClient.addInterceptor(httpLoggingInterceptor)
		}

		return httpClient.build()
	}

	@Provides
	@Singleton
	internal fun provideGson(): Gson {
		val builder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
		return builder.setLenient().create()
	}

	@Provides
	@Singleton
	internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {

		val builder: Retrofit.Builder = Retrofit.Builder()
			.baseUrl(NEWS_API_BASE_URL)
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())

		return builder.build()
	}

	@Provides
	@Singleton
	internal fun getApiCallInterface(retrofit: Retrofit): APIServices {
		return retrofit.create(APIServices::class.java)
	}

	@Provides
	@Singleton
	fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
		val interceptor = HttpLoggingInterceptor()
		interceptor.level = HttpLoggingInterceptor.Level.BODY
		return interceptor
	}
}
