package com.appcake.modsforvalheim.data.di

import android.content.Context
import com.appcake.modsforvalheim.core.repo.ValheimRepo
import com.appcake.modsforvalheim.data.remote.ApiService
import com.appcake.modsforvalheim.data.remote.RemoteDataSource
import com.appcake.modsforvalheim.utlis.AppConstants
import com.appcake.modsforvalheim.utlis.NetworkInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        // okHttpClient.addInterceptor(AuthInterceptor(context))
        okHttpClient.addInterceptor(NetworkInterceptor(context))
        //   okHttpClient.addInterceptor(AuthApiKeyInterceptor(context))
        okHttpClient.hostnameVerifier { hostname, session -> true }
        okHttpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        okHttpClient.readTimeout(60, TimeUnit.SECONDS)
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS)
        return okHttpClient.build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)


    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource
    ) =
        ValheimRepo(remoteDataSource)

}