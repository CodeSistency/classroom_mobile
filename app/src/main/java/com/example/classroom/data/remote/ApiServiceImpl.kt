package com.example.classroom.data.remote

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.*
import kotlinx.serialization.json.JsonPrimitive
import proyecto.person.appconsultapopular.common.Constants
import proyecto.person.appconsultapopular.common.HttpRoutes
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.common.json
import com.example.classroom.common.parseResponseToGenericObject
import com.example.classroom.data.remote.dto.activities.DeleteActivityResponseDto
import com.example.classroom.data.remote.dto.activities.GetActivitiesWithQuizzResponseDto
import com.example.classroom.data.remote.dto.chat.ChatRoomDTO
import com.example.classroom.data.remote.dto.chat.MessageDTO
import com.example.classroom.data.remote.dto.chat.TypingStatusDTO
import com.example.classroom.data.remote.dto.chat.UserStatusDTO
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
import com.example.classroom.data.remote.dto.courses.DeleteCourseResponseDto
import com.example.classroom.data.remote.dto.courses.GetUsersByCourseResponse
import com.example.classroom.data.remote.dto.evaluations.evaluationsSent.EvaluationsSentResponseDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.reviewEvaluationDto.ReviewEvaluationsResponseDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationRequestDto
import com.example.classroom.data.remote.dto.evaluations.sendEvaluationRequestDto.SendEvaluationResponseDto
import com.example.classroom.data.remote.dto.posts.GetPostsResponseDto
import com.example.classroom.data.remote.dto.posts.PostRequestDto
import com.example.classroom.data.remote.dto.posts.PostResponseDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzDto
import com.example.classroom.data.remote.dto.quizz.AnswerQuizzResponseDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzDto
import com.example.classroom.data.remote.dto.quizz.CreateQuizzResponseDto
import com.example.classroom.data.remote.dto.quizz.QuizzResponseDto
import com.example.classroom.domain.model.entity.areatoInt
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import okhttp3.Response
import java.io.File
import java.util.UUID

class ApiServiceImpl(private val client: HttpClient): ApiService {
    override suspend fun signInUser(signInRequestDto: SignInRequestDto): ResponseGenericAPi<SignInResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = signInUserInner(signInRequestDto)
        return@withContext parseResponseToGenericObject(response, true)
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

        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun signUpUserInner(signUpRequestDto: SignUpRequestDto): HttpResponse {
        val randomUsername = "${signUpRequestDto.name}_${UUID.randomUUID()}"
//        val json = buildJsonObject {
//            put("name", signUpRequestDto.name)
//            put("user_name", randomUsername)
//            put("last_name", signUpRequestDto.lastname)
//            put("email", signUpRequestDto.email)
//            put("phone", signUpRequestDto.phone)
//            put("genderId", gendertoInt(signUpRequestDto.gender))
//            put("password", signUpRequestDto.password)
//        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.SIGNUP_ENDPOINT}")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.SIGNUP_ENDPOINT}")
            contentType(ContentType.Application.Json)
            setBody(signUpRequestDto)
        }

        return response
    }

    override suspend fun insertCourseRemote(course: CourseRequestDto): ResponseGenericAPi<CourseResponseDto> = withContext(
    Dispatchers.IO)  {
        val response = courseInnerMethod(course, null)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun courseInnerMethod(courseRequestDto: CourseRequestDto, id: String?): HttpResponse {
        val json = buildJsonObject {
            put("title", courseRequestDto.title)
            put("description", courseRequestDto.description ?: "")
            put("ownerId", courseRequestDto.ownerId)
            put("section", courseRequestDto.section)
            put("subject", courseRequestDto.subject)
            put("owner_name", courseRequestDto.ownerName)
            put("ownerId", courseRequestDto.ownerId)
            put("areaId", courseRequestDto.areaId)
            put("token", "5")
//            put("area", areatoInt(courseRequestDto.area))
        }

        val jsonUpdate = buildJsonObject {
            put("id", id)
            put("title", courseRequestDto.title)
            put("description", courseRequestDto.description ?: "")
            put("section", courseRequestDto.section)
            put("subject", courseRequestDto.subject)
            put("owner_name", courseRequestDto.ownerName)
            put("ownerId", courseRequestDto.ownerId)
            put("areaId", courseRequestDto.areaId)
//            put("token", "")
//            put("area", areatoInt(courseRequestDto.area))
        }

        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
        val method = if (id != null) "update" else "new"

        val response = if (id != null) {
            App.appModule.apiClient.put{

                url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
                contentType(ContentType.Application.Json)
                body = jsonUpdate.toString()
            }
        }else{
            App.appModule.apiClient.post{
                url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}")
                contentType(ContentType.Application.Json)
                body = json.toString()
            }
        }
        return response
    }

    override suspend fun updateCourseRemote(course: CourseRequestDto, id: String): ResponseGenericAPi<CourseResponseDto> =  withContext(
    Dispatchers.IO)  {
        val response = courseInnerMethod(course, id)
        return@withContext parseResponseToGenericObject(response, true)
    }

    override suspend fun deleteCourseRemote(id: String): ResponseGenericAPi<DeleteCourseResponseDto> = withContext(
    Dispatchers.IO)  {
        val response = client.delete{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)
    }

    override suspend fun getCoursesRemote(id: String): ResponseGenericAPi<GetCoursesResponseDto> = withContext(
    Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/find/course/users/${id}")
//            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/find/course/users/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)
    }

    override suspend fun getCourseByIdRemote(id: String): ResponseGenericAPi<CourseResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)
    }

    override suspend fun getUsersByCourseRemote(id: String): ResponseGenericAPi<GetUsersByCourseResponse> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/users/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)
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
            url("${Constants.BASE_URL}${HttpRoutes.COURSES_ENDPOINT}/join")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, true)

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
        return@withContext parseResponseToGenericObject(response, true)

    }
    @OptIn(InternalAPI::class)
    override suspend fun insertActivityRemote(activity: ActivityRequestDto): ResponseGenericAPi<ActivityResponseDto> = withContext(
        Dispatchers.IO)  {
//        val json = buildJsonObject {
//            put("course_id", activity.idCourse.toInt())
//            put("title", activity.title)
//            put("description", activity.description)
//            put("grade", activity.grade)
//            put("email", activity.email)
//            put("end_date", activity.endDate)
//            put("start_date", activity.startDate)
//            put("status_id", activity.status.id)
//        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/new")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}")
            contentType(ContentType.Application.Json)
            setBody(activity)
        }
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    override suspend fun updateActivityRemote(activity: ActivityRequestDto, id: String): ResponseGenericAPi<ActivityResponseDto> = withContext(
        Dispatchers.IO)  {
        val json = buildJsonObject {
            put("id", id.toInt())
            put("title", activity.title)
            put("course_id", activity.idCourse)
            put("description", activity.description)
            put("grade", activity.grade)
            put("endDate", activity.endDate)
            put("startDate", activity.startDate)
        }
        Log.e("RUTA:", "${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}")
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    override suspend fun deleteActivityRemote(id: String) : ResponseGenericAPi<DeleteActivityResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.delete{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
//            body = json.toString()
        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun getActivitiesRemote(id: String): ResponseGenericAPi<GetActivitiesResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun getActivitiesByCourseRemote(id: String): ResponseGenericAPi<GetActivitiesWithQuizzResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/course/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun getActivitiesByUserRemote(id: String): ResponseGenericAPi<GetActivitiesResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/mine/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun getActivitiesWithFlowRemote(): Flow<List<LocalActivities>> {
        TODO("Not yet implemented")
    }

    override suspend fun getActivitiesSentByStudent(
        courseId: String,
        userId: String
    ): ResponseGenericAPi<EvaluationsSentResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/send/course/user/${userId}/${courseId}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)

    }


    override suspend fun studentSendsEvaluation(body: SendEvaluationRequestDto): ResponseGenericAPi<SendEvaluationResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.STUDENT_SEND_ACTIVITY}")
            contentType(ContentType.Application.Json)
            setBody(body) // Ensure proper serialization of body

        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun professorReviewsEvaluation(body: ReviewEvaluationRequestDto): ResponseGenericAPi<ReviewEvaluationsResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.ACTIVITIES_ENDPOINT}/assess/activity")
            contentType(ContentType.Application.Json)
            setBody(body) // Ensure proper serialization of body


        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun uploadFile(fileUri: Uri, context: Context, useSupabase: Boolean): ResponseGenericAPi<CloudResposeDto> =
        withContext(Dispatchers.IO) {
            // Extract the original file name
            val originalFileName = extractOriginalFileName(fileUri, context)

            // Open the file as a stream and convert to bytes
            val fileBytes = context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
                inputStream.readBytes()
            } ?: throw IllegalArgumentException("Unable to read file from URI: $fileUri")

            // Log file details
            Log.d("FILE_UPLOAD", "File URI: $fileUri")
            Log.d("FILE_UPLOAD", "Original file name: $originalFileName")
            Log.d("FILE_UPLOAD", "File size: ${fileBytes.size} bytes")

            // Make the POST request
            val response = client.submitFormWithBinaryData(
                url = "${Constants.BASE_URL}/cloud/send/file",

//                url = "https://class-room-nest.onrender.com/cloud/send/file",
                formData = formData {
                    append("file", fileBytes, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=${originalFileName}")
                    })
                    append("useSupabase", useSupabase.toString())
                }
            )

            Log.e("response", response.bodyAsText())


            // Log response details
//            val rawResponse = response.bodyAsText()
//            Log.d("FILE_UPLOAD", "Response code: ${response.status.value}")
//            Log.d("FILE_UPLOAD", "Response body: $rawResponse")

            return@withContext parseResponseToGenericObject(response, true)
        }

    // Helper function to extract the original filename from the URI
    private fun extractOriginalFileName(fileUri: Uri, context: Context): String {
        val cursor = context.contentResolver.query(fileUri, null, null, null, null)
        cursor?.use {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                return cursor.getString(nameIndex)
            }
        }
        return "uploaded_file_${System.currentTimeMillis()}.png" // Fallback if the filename cannot be determined
    }




    override suspend fun getPostByCourseRemote(id: String): ResponseGenericAPi<GetPostsResponseDto> = withContext(
    Dispatchers.IO)  {
        val response = client.get{
            url("${Constants.BASE_URL}${HttpRoutes.POSTS_ENDPOINT}/course/${id}")
            contentType(ContentType.Application.Json)



        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun createPostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.POSTS_ENDPOINT}")
            contentType(ContentType.Application.Json)
            setBody(body)


        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun updatePostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.put{
            url("${Constants.BASE_URL}${HttpRoutes.POSTS_ENDPOINT}")
            contentType(ContentType.Application.Json)
            setBody(body)


        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun deletePostRemote(id: String): ResponseGenericAPi<PostResponseDto>  = withContext(
        Dispatchers.IO)  {
        val response = client.delete{
            url("${Constants.BASE_URL}${HttpRoutes.POSTS_ENDPOINT}/${id}")
            contentType(ContentType.Application.Json)
        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun createQuizzRemote(body: CreateQuizzDto): ResponseGenericAPi<CreateQuizzResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.QUIZZ_ENDPOINT}/new")
            contentType(ContentType.Application.Json)
            setBody(body)


        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    override suspend fun answerQuizzRemote(body: AnswerQuizzDto, idQuizz: String): ResponseGenericAPi<AnswerQuizzResponseDto> = withContext(
        Dispatchers.IO)  {
        val response = client.post{
            url("${Constants.BASE_URL}${HttpRoutes.QUIZZ_ENDPOINT}/${idQuizz}/answer")
            contentType(ContentType.Application.Json)
            setBody(body)


        }
        return@withContext parseResponseToGenericObject(response, true)

    }

    //CHATS

    override suspend fun getOrCreatePrivateRoom(userId1: Int, userId2: Int): ResponseGenericAPi<ChatRoomDTO> = withContext(Dispatchers.IO) {
        val response = getOrCreatePrivateRoomInner(userId1, userId2)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun getOrCreatePrivateRoomInner(userId1: Int, userId2: Int): HttpResponse {
        val json = buildJsonObject {
            put("userId1", userId1)
            put("userId2", userId2)
        }

        val response = client.post {
            url("${Constants.BASE_URL}")
//            url("${Constants.BASE_URL}${HttpRoutes.GET_OR_CREATE_PRIVATE_ROOM}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }

        return response
    }

    override suspend fun createGroupRoom(userId: Int, userIds: List<Int>, roomName: String): ResponseGenericAPi<ChatRoomDTO> = withContext(Dispatchers.IO) {
        val response = createGroupRoomInner(userId, userIds, roomName)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun createGroupRoomInner(userId: Int, userIds: List<Int>, roomName: String): HttpResponse {
        val json = buildJsonObject {
            put("userId", userId)
            put("roomName", roomName)
            put("userIds", JsonArray(userIds.map { JsonPrimitive(it) }))
        }

        val response = client.post {
            url("${Constants.BASE_URL}")
//            url("${Constants.BASE_URL}${HttpRoutes.CREATE_GROUP_ROOM}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }

        return response
    }

    override suspend fun sendMessage(userId: Int, roomId: Int, content: String, messageType: String): ResponseGenericAPi<MessageDTO> = withContext(Dispatchers.IO) {
        val response = sendMessageInner(userId, roomId, content, messageType)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun sendMessageInner(userId: Int, roomId: Int, content: String, messageType: String): HttpResponse {
        val json = buildJsonObject {
            put("userId", userId)
            put("roomId", roomId)
            put("content", content)
            put("messageType", messageType)
        }

        val response = client.post {
            url("${Constants.BASE_URL}")
//            url("${Constants.BASE_URL}${HttpRoutes.SEND_MESSAGE}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }

        return response
    }

    override suspend fun updateTypingStatus(userId: Int, roomId: Int, isTyping: Boolean): ResponseGenericAPi<TypingStatusDTO> = withContext(Dispatchers.IO) {
        val response = updateTypingStatusInner(userId, roomId, isTyping)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun updateTypingStatusInner(userId: Int, roomId: Int, isTyping: Boolean): HttpResponse {
        val json = buildJsonObject {
            put("userId", userId)
            put("roomId", roomId)
            put("isTyping", isTyping)
        }

        val response = client.post {
            url("${Constants.BASE_URL}")
//            url("${Constants.BASE_URL}${HttpRoutes.UPDATE_TYPING_STATUS}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }

        return response
    }

    override suspend fun updateUserStatus(userId: Int, isOnline: Boolean): ResponseGenericAPi<UserStatusDTO> = withContext(Dispatchers.IO) {
        val response = updateUserStatusInner(userId, isOnline)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun updateUserStatusInner(userId: Int, isOnline: Boolean): HttpResponse {
        val json = buildJsonObject {
            put("userId", userId)
            put("isOnline", isOnline)
        }

        val response = client.post {
            url("${Constants.BASE_URL}")
//            url("${Constants.BASE_URL}${HttpRoutes.UPDATE_USER_STATUS}")
            contentType(ContentType.Application.Json)
            body = json.toString()
        }

        return response
    }

    override suspend fun getMessages(roomId: Int): ResponseGenericAPi<List<MessageDTO>> = withContext(Dispatchers.IO) {
        val response = getMessagesInner(roomId)
        return@withContext parseResponseToGenericObject(response, true)
    }

    @OptIn(InternalAPI::class)
    private suspend fun getMessagesInner(roomId: Int): HttpResponse {
        val response = client.get {
            url("${Constants.BASE_URL}")
//            url("${Constants.BASE_URL}${HttpRoutes.GET_MESSAGES}/$roomId")
        }

        return response
    }


}