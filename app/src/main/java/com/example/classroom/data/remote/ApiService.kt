package com.example.classroom.data.remote

import android.content.Context
import android.net.Uri
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
import kotlinx.coroutines.flow.Flow
import com.example.classroom.common.ResponseGenericAPi
import com.example.classroom.data.remote.dto.activities.GetActivitiesWithQuizzResponseDto
import com.example.classroom.data.remote.dto.chat.ChatRoomDTO
import com.example.classroom.data.remote.dto.chat.MessageDTO
import com.example.classroom.data.remote.dto.chat.TypingStatusDTO
import com.example.classroom.data.remote.dto.chat.UserStatusDTO
import com.example.classroom.data.remote.dto.cloud.CloudResposeDto
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
import io.ktor.client.statement.HttpResponse
import okhttp3.Response
import java.io.File

interface ApiService {

    //User Methods
    suspend fun signInUser(signInRequestDto: SignInRequestDto): ResponseGenericAPi<SignInResponseDto>
    suspend fun signUpUser(signUpRequestDto: SignUpRequestDto): ResponseGenericAPi<SignUpResponseDto>

    //Course Methods
    suspend fun insertCourseRemote(course: CourseRequestDto) : ResponseGenericAPi<CourseResponseDto>
    suspend fun updateCourseRemote(course: CourseRequestDto, id: String) : ResponseGenericAPi<CourseResponseDto>
    suspend fun deleteCourseRemote(id: String) : ResponseGenericAPi<Boolean>
    suspend fun getCoursesRemote(id: String) : ResponseGenericAPi<GetCoursesResponseDto>
    suspend fun getCourseByIdRemote(id: String) : ResponseGenericAPi<CourseResponseDto>
    suspend fun getUsersByCourseRemote(id: String) : ResponseGenericAPi<GetUsersByCourseResponse>
    suspend fun getCoursesWithFlowRemote() : Flow<List<LocalCourses>>
    suspend fun joinCourseRemote(id: String, token: String) : ResponseGenericAPi<CourseResponseDto>
    suspend fun joinUserToCourseRemote(id: String, token: String) : ResponseGenericAPi<CourseResponseDto>

    //Activity Methods
    suspend fun insertActivityRemote(activity: ActivityRequestDto) : ResponseGenericAPi<ActivityResponseDto>
    suspend fun updateActivityRemote(activity: ActivityRequestDto, id: String) : ResponseGenericAPi<ActivityResponseDto>
    suspend fun deleteActivityRemote(id: String) : ResponseGenericAPi<Boolean>
    suspend fun getActivitiesRemote(id: String) : ResponseGenericAPi<GetActivitiesResponseDto>
    suspend fun getActivitiesByCourseRemote(id: String) : ResponseGenericAPi<GetActivitiesWithQuizzResponseDto>
    suspend fun getActivitiesByUserRemote(id: String) : ResponseGenericAPi<GetActivitiesResponseDto>
    suspend fun getActivitiesWithFlowRemote() : Flow<List<LocalActivities>>



    //EVALUATIONS
    suspend fun getActivitiesSentByStudent(courseId: String, userId: String): ResponseGenericAPi<EvaluationsSentResponseDto>
    suspend fun studentSendsEvaluation(body: SendEvaluationRequestDto): ResponseGenericAPi<SendEvaluationResponseDto>
    suspend fun professorReviewsEvaluation(body: ReviewEvaluationRequestDto): ResponseGenericAPi<ReviewEvaluationsResponseDto>

    //CLOUD
    suspend fun uploadFile(fileUri: Uri, context: Context, useSupabase: Boolean): ResponseGenericAPi<CloudResposeDto>

    //POSTS
    suspend fun getPostByCourseRemote(id: String): ResponseGenericAPi<GetPostsResponseDto>

    suspend fun createPostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto>

    suspend fun updatePostRemote(body: PostRequestDto): ResponseGenericAPi<PostResponseDto>

    suspend fun deletePostRemote(id: String): ResponseGenericAPi<PostResponseDto>

    suspend fun createQuizzRemote(body: CreateQuizzDto): ResponseGenericAPi<CreateQuizzResponseDto>

    suspend fun answerQuizzRemote(body: AnswerQuizzDto, idQuizz: String): ResponseGenericAPi<AnswerQuizzResponseDto>



    //CHATS

    // Get or Create a private chat room between two users
    suspend fun getOrCreatePrivateRoom(userId1: Int, userId2: Int): ResponseGenericAPi<ChatRoomDTO>

    // Create a group chat room
    suspend fun createGroupRoom(userId: Int, userIds: List<Int>, roomName: String): ResponseGenericAPi<ChatRoomDTO>

    // Send a message to a chat room
    suspend fun sendMessage(userId: Int, roomId: Int, content: String, messageType: String): ResponseGenericAPi<MessageDTO>

    // Update typing status for a user in a chat room
    suspend fun updateTypingStatus(userId: Int, roomId: Int, isTyping: Boolean): ResponseGenericAPi<TypingStatusDTO>

    // Update user status (online/offline)
    suspend fun updateUserStatus(userId: Int, isOnline: Boolean): ResponseGenericAPi<UserStatusDTO>

    // Get messages from a chat room
    suspend fun getMessages(roomId: Int): ResponseGenericAPi<List<MessageDTO>>
}
