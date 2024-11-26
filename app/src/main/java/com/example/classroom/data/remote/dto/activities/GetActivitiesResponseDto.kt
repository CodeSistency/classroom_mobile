package com.example.classroom.data.remote.dto.activities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.classroom.data.remote.dto.courses.CourseResponseDto
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class GetActivitiesResponseDto(
//    @SerialName("status")
//    val status: Boolean,
//    @SerialName("message")
//    val message: String,
//    @SerialName("data")
//    val data: List<Data>
//) {
//
//
//        @Serializable
//        data class Data(
//            @SerialName("id")
//            val idApi: Int,
//            @SerialName("course_id")
//            val idCourse: Int,
//            @SerialName("course_name")
//            val nameCourse: String,
//            @SerialName("title")
//            val title: String,
//            @SerialName("description")
//            val description: String?,
//            @SerialName("grade")
//            val grade: Int,
//            @SerialName("start_date")
//            val startDate: String,
//            @SerialName("end_date")
//            val endDate: String,
//            @SerialName("status_id")
//            val status: Int,
//            @SerialName("status_name")
//            val statusName: String
//        )
//
//}

@Serializable
data class GetActivitiesResponseDto(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<Data>
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val idApi: Int,
        @SerialName("course_id")
        val idCourse: Int,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String?,
        @SerialName("grade")
        val grade: Double,
        @SerialName("start_date")
        val startDate: String,
        @SerialName("end_date")
        val endDate: String,
        @SerialName("status_id")
        val status: Int,
        @SerialName("email")
        val email: String
    )
}

@Serializable
data class GetActivitiesWithQuizzResponseDto(
    @SerialName("code")
    val status: Int,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<Activity>
) {
    @Serializable
    data class Activity(
        @SerialName("id")
        val idApi: Int,
        @SerialName("course_id")
        val idCourse: Int,
        @SerialName("title")
        val title: String,
        @SerialName("description")
        val description: String?,
        @SerialName("grade")
        val grade: Double,
        @SerialName("start_date")
        val startDate: String,
        @SerialName("end_date")
        val endDate: String,
        @SerialName("email")
        val email: String,
        @SerialName("digital")
        val digital: Boolean,
        @SerialName("isQuizz")
        val isQuizz: Boolean,
        @SerialName("status_id")
        val status: Int,
        @SerialName("quizzId")
        val quizzId: Int?,
        @SerialName("questions")
        val questions: List<Question>
    ) {
        @Serializable
        data class Question(
            @SerialName("id")
            val id: Int,
            @SerialName("text")
            val text: String,
            @SerialName("answer")
            val answer: Int,
            @SerialName("options")
            val options: List<Option>
        )

        @Serializable
        data class Option(
            @SerialName("id")
            val id: Int,
            @SerialName("text")
            val text: String
        )
    }
}
