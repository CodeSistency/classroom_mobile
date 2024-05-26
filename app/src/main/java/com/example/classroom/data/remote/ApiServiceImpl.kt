package com.example.classroom.data.remote

import android.util.Log
import com.example.classroom.App
import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.activities.ActivityResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesResponseDto
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.courses.CourseResponseDto
import com.example.classroom.data.remote.dto.courses.GetCoursesResponseDto
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signIn.SignInResponseDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpResponseDto
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.gendertoInt
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import proyecto.person.appconsultapopular.common.Constants
import proyecto.person.appconsultapopular.common.HttpRoutes
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.common.json
import com.example.classroom.common.parseResponseToGenericObject
import com.example.classroom.domain.model.entity.areatoInt
import io.ktor.client.request.delete
import io.ktor.client.request.get
import java.util.UUID

class ApiServiceImpl(private val client: HttpClient): ApiService {
    override suspend fun signInUser(signInRequestDto: SignInRequestDto): ResponseGenericAPi<SignInResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = signInUserInner(signInRequestDto)
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    @OptIn(InternalAPI::class)
    private suspend fun signInUserInner(signInRequestDto: SignInRequestDto): HttpResponse {
        val json = buildJsonObject {
            put("email", signInRequestDto.email)
            put("password", signInRequestDto.password)
        }

        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.SIGNIN_ENDPOINT}")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.SIGNIN_ENDPOINT}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return response
    }

    override suspend fun signUpUser(signUpRequestDto: SignUpRequestDto): ResponseGenericAPi<SignUpResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = signUpUserInner(signUpRequestDto)
//        Log.e("Entro", "${Constants.BASE_URL}${HttpRoutes.SIGNUP_ENDPOINT}")

        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    @OptIn(InternalAPI::class)
    private suspend fun signUpUserInner(signUpRequestDto: SignUpRequestDto): HttpResponse {
        val randomUsername = "${signUpRequestDto.name}_${UUID.randomUUID()}"
        val json = buildJsonObject {
            put("name", signUpRequestDto.name)
            put("user_name", randomUsername)
            put("last_name", signUpRequestDto.lastname)
            put("email", signUpRequestDto.email)
            put("phone", signUpRequestDto.phone)
            put("genderId", gendertoInt(signUpRequestDto.gender))
            put("password", signUpRequestDto.password)
        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.SIGNUP_ENDPOINT}")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.SIGNUP_ENDPOINT}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }

        return response
    }

    override suspend fun insertCourseRemote(course: CourseRequestDto): ResponseGenericAPi<CourseResponseDto> = withContext(
    Dispatchers.IO)  {
        val response = courseInnerMethod(course, null)
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    @OptIn(InternalAPI::class)
    private suspend fun courseInnerMethod(courseRequestDto: CourseRequestDto, id: String?): HttpResponse {

        val json = buildJsonObject {
            put("title", courseRequestDto.title)
            put("description", courseRequestDto.description ?: "")
            put("owner_id", courseRequestDto.owner)
            put("section", courseRequestDto.section)
            put("subject", courseRequestDto.subject)
            put("area", areatoInt(courseRequestDto.area))
        }

        val jsonUpdate = buildJsonObject {
            put("title", courseRequestDto.title)
            put("description", courseRequestDto.description ?: "")
            put("section", courseRequestDto.section)
            put("subject", courseRequestDto.subject)
            put("area", areatoInt(courseRequestDto.area))
        }

        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
        val method = if (id != null) "update" else "new"

        val response = if (id != null) {
            App.appModule.apiClient.put{

                url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}${method}")
                contentType(ContentType.Application.Json)
                body = jsonUpdate.toString()
            }
        }else{
            App.appModule.apiClient.post{
                url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}${method}")
                contentType(ContentType.Application.Json)
                body = json.toString()
            }
        }
        return response
    }

    override suspend fun updateCourseRemote(course: CourseRequestDto, id: String): ResponseGenericAPi<CourseResponseDto> =  withContext(
    Dispatchers.IO)  {
        val response = courseInnerMethod(course, id)
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    override suspend fun deleteCourseRemote(id: String): ResponseGenericAPi<Boolean> = withContext(
    Dispatchers.IO)  {
        val response = client.delete{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    override suspend fun getCoursesRemote(id: String): ResponseGenericAPi<GetCoursesResponseDto> = withContext(
    Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/mine/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    override suspend fun getCourseByIdRemote(id: String): ResponseGenericAPi<CourseResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    override suspend fun getUsersByCourseRemote(id: String): ResponseGenericAPi<GetCoursesResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/users/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    override suspend fun getCoursesWithFlowRemote(): Flow<List<LocalCourses>> {
        TODO("Not yet implemented")
    }

    @OptIn(InternalAPI::class)
    override suspend fun joinCourseRemote(
        id: String,
        token: String
    ): ResponseGenericAPi<CourseResponseDto> = withContext(
        Dispatchers.IO)  {
        val json = buildJsonObject {
            put("id", id.toInt())
            put("token", token)
        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/join")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)

    }

    @OptIn(InternalAPI::class)
    override suspend fun joinUserToCourseRemote(
        id: String,
        token: String
    ): ResponseGenericAPi<CourseResponseDto> = withContext(
        Dispatchers.IO)  {
        val json = buildJsonObject {
            put("id", id.toInt())
            put("token", token)
        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)

    }
    @OptIn(InternalAPI::class)
    override suspend fun insertActivityRemote(activity: ActivityRequestDto): ResponseGenericAPi<ActivityResponseDto> = withContext(
        Dispatchers.IO)  {
        val json = buildJsonObject {
            put("title", activity.title)
            put("description", activity.description)
            put("grade", activity.grade)
            put("endDate", activity.endDate)
            put("startDate", activity.startDate)
        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/new")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateActivityRemote(activity: ActivityRequestDto, id: String): ResponseGenericAPi<ActivityResponseDto> = withContext(
        Dispatchers.IO)  {
        val json = buildJsonObject {
            put("title", activity.title)
            put("description", activity.description)
            put("grade", activity.grade)
            put("endDate", activity.endDate)
            put("startDate", activity.startDate)
        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/update")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)
    }

    @OptIn(InternalAPI::class)
    override suspend fun deleteActivityRemote(id: String) : ResponseGenericAPi<Boolean> = withContext(
        Dispatchers.IO)  {
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)

    }

    override suspend fun getActivitiesRemote(id: String): ResponseGenericAPi<GetActivitiesResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/activity/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)

    }

    override suspend fun getActivitiesByCourseRemote(id: String): ResponseGenericAPi<GetActivitiesResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, response.status == HttpStatusCode.OK)

    }

    override suspend fun getActivitiesWithFlowRemote(): Flow<List<LocalActivities>> {
        TODO("Not yet implemented")
    }


}