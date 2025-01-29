package com.example.classroom.data.remote.dto.quizz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



//@Serializable
//data class QuizOptionDto(
//    val id: Int,
//    val text: String,
//    @SerialName("questionId")
//    val questionId: Int
//)
//
//@Serializable
//data class QuizQuestionDto(
//    val id: Int,
//    @SerialName("quizzId")
//    val quizzId: Int?,  // Nullable to handle its absence from the provided example
//    val text: String,
//    val answer: Int,
//    val options: List<QuizOptionDto>
//)
//@Serializable
//data class QuizActivityDto(
//    val id: Int,
//    @SerialName("course_id")
//    val courseId: Int,
//    val title: String,
//    val description: String,
//    val grade: Double,
//    @SerialName("start_date")
//    val startDate: String,
//    @SerialName("end_date")
//    val endDate: String,
//    val email: String,
//    val digital: Boolean,
//    val isQuizz: Boolean,
//    @SerialName("status_id")
//    val statusId: Int
//)
//
//@kotlinx.serialization.Serializable
//data class QuizSubmissionDto(
//    val id: Int,
//    val quizzId: Int,
//    val userId: Int,
//    val grade: Double,
//    val answers: List<SubmittedAnswerDto>
//)
//
//@kotlinx.serialization.Serializable
//data class SubmittedAnswerDto(
//    val id: Int,
//    val optionId: Int
//)
//
//@kotlinx.serialization.Serializable
//data class AnswerQuizzResponseDto(
//    val code: Int,
//    val message: String,
//    val data: AnswerQuizzDataDto
//)
//
//@kotlinx.serialization.Serializable
//data class AnswerQuizzDataDto(
//    val grade: Double,
//    val submission: QuizSubmissionDto,
//    val quizz: QuizDto,
//    val quizzId: Int, // Add the quiz ID explicitly
//)
//
////@Serializable
////data class QuizDto(
////    val id: Int,
////    val activityId: Int,
////    val activity: QuizActivityDto,
////    val question: List<QuizQuestionDto>
////)
//
//@Serializable
//data class QuizDto(
//    val id: Int,
//    val activityId: Int? = null, // Nullable to handle its absence
//    val activity: QuizActivityDto? = null, // Nullable to handle its absence
//    val question: List<QuizQuestionDto>? = null // Nullable to handle its absence
//)
//
//








@Serializable
data class QuizActivityDto(
    val id: Int,
    @SerialName("course_id")
    val courseId: Int, // Marked as nullable to handle missing data
    val title: String,
    val description: String,
    @SerialName("grade")
    val grade: Double = 0.0, // Make grade nullable if the response can have missing values
    @SerialName("startDate")
    val startDate: String = "", // Nullable to handle missing values
    @SerialName("endDate")
    val endDate: String = "", // Nullable to handle missing values
    val email: String,
    val digital: Boolean,
    val post: PostDto? = null, // Added `post` field
    val isQuizz: Boolean,
    @SerialName("statusId")
    val statusId: Int? = null // Nullable to handle missing values
)

//@Serializable
//data class QuizActivityDto(
//    val id: Int,
//    @SerialName("courseId")
//    val courseId: Int, // Corrected naming to match response
//    val title: String,
//    val description: String,
//    @SerialName("grade")
//    val grade: Double, // Changed to String to match the response format
//    @SerialName("startDate")
//    val startDate: String, // Corrected naming to match response
//    @SerialName("endDate")
//    val endDate: String, // Corrected naming to match response
//    val email: String,
//    val digital: Boolean,
//    val isQuizz: Boolean,
//    @SerialName("statusId")
//    val statusId: Int // Corrected naming to match response
//)

//@Serializable
//data class SubmittedAnswerDto(
//    val id: Int,
//    val optionId: Int
//)
//
//@Serializable
//data class QuizSubmissionDto(
//    val id: Int,
//    val quizzId: Int,
//    val userId: Int,
//    @SerialName("grade")
//    val grade: String, // Changed to String to match the response format
//    @SerialName("create_date")
//    val createDate: String, // Corrected naming to match response
//    val answers: List<SubmittedAnswerDto>
//)
//
//@Serializable
//data class QuizDto(
//    val id: Int,
//    val activityId: Int,
//    val activity: QuizActivityDto,
//    val question: List<QuizQuestionDto>,
//    val title: String,
//    val description: String,
//    val totalQuestions: Int
//)
//
//
//
////@Serializable
////data class AnswerQuizzDataDto(
////    val grade: Double,
////    val submission: QuizSubmissionDto,
////    val quizz: QuizDto
////)
//@Serializable
//data class AnswerQuizzDataDto(
//    @SerialName("grade")
//    val grade: String, // Change from Double to String
//    val submission: QuizSubmissionDto,
//    val quizz: QuizDto
//)
//
//
//
//
//
//
//@Serializable
//data class AnswerQuizzResponseDto(
//    val code: Int,
//    val message: String,
//    val data: AnswerQuizzDataDto
//)

@Serializable
data class SubmittedAnswerDto(
    val id: Int,
    val optionId: Int
)

@Serializable
data class QuizSubmissionDto(
    val id: Int,
    val quizzId: Int,
    val userId: Int,
    @SerialName("grade")
    val grade: String, // Matches "100.00"
    @SerialName("create_date")
    val createDate: String, // Matches "2025-01-29T15:20:05.142Z"
    val answers: List<SubmittedAnswerDto>
)

@Serializable
data class QuizOptionDto(
    val id: Int,
    val text: String,
    @SerialName("questionId")
    val questionId: Int
)

@Serializable
data class QuizQuestionDto(
    val id: Int,
    val quizzId: Int,
    val text: String,
    val answer: Int,
    val options: List<QuizOptionDto>
)

@Serializable
data class QuizActivityDto2(
    val id: Int,
    val courseId: Int,
    val title: String,
    val description: String,
    @SerialName("grade")
    val grade: String, // Matches "0"
    @SerialName("startDate")
    val startDate: String, // Matches "02/01/2025"
    @SerialName("endDate")
    val endDate: String, // Matches "31/01/2025"
    val email: String,
    val digital: Boolean,
    @SerialName("isQuizz")
    val isQuizz: Boolean,
    val statusId: Int
)

@Serializable
data class QuizDto2(
    val id: Int,
    val activityId: Int,
    val activity: QuizActivityDto2,
    val question: List<QuizQuestionDto>,
    val title: String,
    val description: String,
    val totalQuestions: Int
)

@Serializable
data class AnswerQuizzDataDto(
    @SerialName("grade")
    val grade: Int, // Matches 100 from JSON
    val submission: QuizSubmissionDto,
    val quizz: QuizDto2
)

@Serializable
data class AnswerQuizzResponseDto(
    val code: Int,
    val message: String,
    val data: AnswerQuizzDataDto
)