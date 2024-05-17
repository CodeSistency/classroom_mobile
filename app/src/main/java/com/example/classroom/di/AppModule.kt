package com.example.classroom.di

import android.content.Context
import androidx.room.Room
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.ApiServiceImpl
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.data.repository.RepositoryBundle
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import proyecto.person.appconsultapopular.data.local.db.AppDatabase


interface AppModule {
    val apiService: ApiService
    val apiClient: HttpClient
    val repositoryBundle: RepositoryBundle

    val db: AppDatabase
}

class AppModuleImpl(
    private val appContext: Context
): AppModule {

    override val apiService: ApiService by lazy {
        ApiServiceImpl(apiClient)
    }

    override val apiClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            expectSuccess = false

            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60000
            }
        }
    }
    override val repositoryBundle: RepositoryBundle by lazy {
        RepositoryBundle(

            loginRepository = LoginRepositoryImpl(apiService, db.appDao),
        )
    }


    override val db: AppDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigrationFrom(1,2,3).build()
    }



}