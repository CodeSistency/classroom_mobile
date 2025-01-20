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
data class QuizOptionDto(
    val id: Int,
    val text: String,
    @SerialName("questionId")
    val questionId: Int
)

@Serializable
data class QuizQuestionDto(
    val id: Int,
    @SerialName("quizzId")
    val quizzId: Int, // Changed to non-nullable since it's present in the response
    val text: String,
    val answer: Int,
    val options: List<QuizOptionDto>
)

@Serializable
data class QuizActivityDto(
    val id: Int,
    @SerialName("courseId")
    val courseId: Int, // Corrected naming to match response
    val title: String,
    val description: String,
    @SerialName("grade")
    val grade: Double, // Changed to String to match the response format
    @SerialName("startDate")
    val startDate: String, // Corrected naming to match response
    @SerialName("endDate")
    val endDate: String, // Corrected naming to match response
    val email: String,
    val digital: Boolean,
    val isQuizz: Boolean,
    @SerialName("statusId")
    val statusId: Int // Corrected naming to match response
)

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
    val grade: String, // Changed to String to match the response format
    @SerialName("create_date")
    val createDate: String, // Corrected naming to match response
    val answers: List<SubmittedAnswerDto>
)

@Serializable
data class QuizDto(
    val id: Int,
    val activityId: Int,
    val activity: QuizActivityDto,
    val question: List<QuizQuestionDto>,
    val title: String,
    val description: String,
    val totalQuestions: Int
)

@Serializable
data class AnswerQuizzDataDto(
    val grade: Double,
    val submission: QuizSubmissionDto,
    val quizz: QuizDto
)

@Serializable
data class AnswerQuizzResponseDto(
    val code: Int,
    val message: String,
    val data: AnswerQuizzDataDto
)