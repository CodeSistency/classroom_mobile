package com.example.classroom.data.remote.dto.quizz


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateQuizzResponseDto(
    val code: Int,
    val message: String,
    val data: CreatedQuizDataDto
)

@Serializable
data class CreatedQuizDataDto(
    val id: Int,
    @SerialName("activity_id")
    val activityId: Int,
    val activity: QuizActivityDto,
    val question: List<QuizQuestionDto>
)



@Serializable
data class PostDto(
    val id: Int,
    val title: String,
    val content: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("courseId")
    val courseId: Int,
    val file: String,
    @SerialName("activityId")
    val activityId: Int,
    @SerialName("authorId")
    val authorId: Int
)

@Serializable
data class QuizzDto(
    val id: Int,
    @SerialName("activity_id")
    val activityId: Int
)

//@Serializable
//data class QuizQuestionDto(
//    val id: Int,
//    @SerialName("quizzId")
//    val quizzId: Int,
//    val text: String,
//    val answer: Int, // Answer is a 1-based index
//    val options: List<QuizOptionDto>
//)
//
//@Serializable
//data class QuizOptionDto(
//    val id: Int,
//    val text: String,
//    @SerialName("questionId")
//    val questionId: Int
//)


//@Serializable
//data class QuizQuestionDto(
//    val id: Int,
//    val quizzId: Int,
//    val text: String,
//    val answer: Int,
//    val options: List<QuizOptionDto>
//)
//
//@Serializable
//data class QuizOptionDto(
//    val id: Int,
//    val text: String,
//    @SerialName("questionId") // Ensure this matches JSON
//    val questionId: Int
//)

//@Serializable
//data class CreateQuizzResponseDto(
//    val code: Int,
//    val message: String,
//    val data: CreatedQuizDataDto
//)
//
//@Serializable
//data class CreatedQuizDataDto(
//    val id: Int,
//    @SerialName("activity_id")
//    val activityId: Int,
//    val activity: QuizActivityDto,
//    val question: List<QuizQuestionDto>
//)
//
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
//    val quizzId: Int, // Changed to non-nullable since it's present in the response
//    val text: String,
//    val answer: Int,
//    val options: List<QuizOptionDto>
//)