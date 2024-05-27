package com.example.classroom.di

import android.content.Context
import androidx.room.Room
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.ApiServiceImpl
import com.example.classroom.data.repository.ActivitiesRepositoryImpl
import com.example.classroom.data.repository.CoursesRepositoryImpl
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.use_case.activities.GetActivitiesByUserUseCase
import com.example.classroom.domain.use_case.activities.GetActivitiesUseCase
import com.example.classroom.domain.use_case.activities.InsertActivityUseCase
import com.example.classroom.domain.use_case.activities.UpdateActivityUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesByIdUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesUseCase
import com.example.classroom.domain.use_case.courses.GetUsersByCourseUseCase
import com.example.classroom.domain.use_case.courses.InsertCourseUseCase
import com.example.classroom.domain.use_case.courses.JoinCourseUseCase
import com.example.classroom.domain.use_case.courses.JoinUserToCourseUseCase
import com.example.classroom.domain.use_case.courses.UpdateCourseUseCase
import com.example.classroom.domain.use_case.signIn.SignInUseCase
import com.example.classroom.domain.use_case.signUp.SignUpUseCase
import com.example.classroom.domain.use_case.validators.ValidatorBundle
import com.example.classroom.domain.use_case.validators.activities.ActivitiesValidator
import com.example.classroom.domain.use_case.validators.cases.ValidateAge
import com.example.classroom.domain.use_case.validators.cases.ValidateDate
import com.example.classroom.domain.use_case.validators.cases.ValidateEmail
import com.example.classroom.domain.use_case.validators.cases.ValidateGrade
import com.example.classroom.domain.use_case.validators.cases.ValidateNames
import com.example.classroom.domain.use_case.validators.cases.ValidatePassword
import com.example.classroom.domain.use_case.validators.cases.ValidatePhone
import com.example.classroom.domain.use_case.validators.cases.ValidateReapeatedPassword
import com.example.classroom.domain.use_case.validators.courses.CoursesValidator
import com.example.classroom.domain.use_case.validators.signIn.SignInValidator
import com.example.classroom.domain.use_case.validators.signUp.SignUpValidator
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
    val signInUseCase: SignInUseCase
    val signUpUseCase: SignUpUseCase
    val updateCourseUseCase: UpdateCourseUseCase
    val insertCourseUseCase: InsertCourseUseCase
    val getCoursesUseCase: GetCoursesUseCase
    val updateActivityUseCase: UpdateActivityUseCase
    val insertActivityUseCase: InsertActivityUseCase
    val getActivitiesUseCase: GetActivitiesUseCase
    val getActivitiesByUserUseCase: GetActivitiesByUserUseCase
    val getCoursesByIdUseCase: GetCoursesByIdUseCase
    val joinUserToCourseUseCase: JoinUserToCourseUseCase
    val joinCourseUseCase: JoinCourseUseCase
    val getUsersByCourseUseCase: GetUsersByCourseUseCase
    val validatorBundle : ValidatorBundle
    val db: AppDatabase
}

class AppModuleImpl(
    private val appContext: Context,
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
            activitiesRepository = ActivitiesRepositoryImpl(apiService, db.appDao),
            coursesRepository = CoursesRepositoryImpl(apiService, db.appDao)
        )
    }
    override val signInUseCase: SignInUseCase by lazy {
        SignInUseCase(repositoryBundle)
    }

    override val signUpUseCase: SignUpUseCase by lazy {
        SignUpUseCase(repositoryBundle)
    }
    override val updateCourseUseCase: UpdateCourseUseCase by lazy {
        UpdateCourseUseCase(repositoryBundle)
    }

    override val insertCourseUseCase: InsertCourseUseCase by lazy {
        InsertCourseUseCase(repositoryBundle)
    }

    override val getCoursesUseCase: GetCoursesUseCase by lazy {
        GetCoursesUseCase(repositoryBundle)
    }

    override val updateActivityUseCase: UpdateActivityUseCase by lazy {
        UpdateActivityUseCase(repositoryBundle)
    }

    override val insertActivityUseCase: InsertActivityUseCase by lazy {
        InsertActivityUseCase(repositoryBundle)
    }

    override val getActivitiesUseCase: GetActivitiesUseCase by lazy {
        GetActivitiesUseCase(repositoryBundle)
    }
    override val getActivitiesByUserUseCase: GetActivitiesByUserUseCase by lazy{
        GetActivitiesByUserUseCase(repositoryBundle)
    }
    override val getCoursesByIdUseCase: GetCoursesByIdUseCase by lazy{
        GetCoursesByIdUseCase(repositoryBundle)
    }
    override val joinUserToCourseUseCase: JoinUserToCourseUseCase by lazy{
        JoinUserToCourseUseCase(repositoryBundle)
    }
    override val joinCourseUseCase: JoinCourseUseCase by lazy{
        JoinCourseUseCase(repositoryBundle)
    }
    override val getUsersByCourseUseCase: GetUsersByCourseUseCase by lazy {
        GetUsersByCourseUseCase(repositoryBundle)
    }

    override val validatorBundle: ValidatorBundle by lazy {
                ValidatorBundle(
                    coursesValidator = CoursesValidator(
                        ValidateNames()
                    ),
                    signInValidator = SignInValidator(
                        ValidateEmail(),
                        ValidatePassword(),
                    ),
                    signUpValidator = SignUpValidator(
                        ValidateNames(),
                        ValidatePassword(),
                        ValidateEmail(),
                        ValidateReapeatedPassword(),
                        ValidateAge(),
                        ValidatePhone()
                    ),
                    activitiesValidator = ActivitiesValidator(
                        ValidateNames(),
                        ValidateDate(),
                        ValidateGrade()
                    )
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