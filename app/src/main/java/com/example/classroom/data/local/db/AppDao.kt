package com.example.classroom.data.local.db

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalStudentEvaluation
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.domain.model.entity.QuizzWithQuestions
import com.example.classroom.domain.model.typeConverter.UsersCoursesIdConverter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map


@Dao
interface AppDao {

    //Users
    @Insert
    suspend fun insertLocalUser(localUser: LocalUser)

    @Update
    suspend fun updateLocalUser(localUser: LocalUser)

    @Query("DELETE FROM localUser_table")
    suspend fun deleteLocalUser()

    @Query("SELECT * FROM localUser_table")
    suspend fun getUserInfo(): List<LocalUser>

    @Query("SELECT * FROM localUser_table")
    fun getUserInfoWithFlow(): Flow<List<LocalUser>>

    @Query("SELECT * FROM localUser_table WHERE idApi = :userId")
    fun getUserById(userId: Int): Flow<LocalUser?>

    @Transaction
    suspend fun logout(){
        deleteLocalUser()
        deleteLocalCourses()
        deleteLocalActivities()
    }


    // New method to get users by course ID
    @Query("SELECT * FROM localUser_table")
    fun getAllUsersWithFlow(): Flow<List<LocalUser>>

  



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLocalUsers(users: List<LocalUser>)

    //Courses
    @Insert
    suspend fun insertLocalCourse(localCourse: LocalCourses)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCourses(course: List<LocalCourses>)

    @Transaction
    suspend fun insertAllLocalCourse(course: List<LocalCourses>){
        deleteLocalCourses()
        delay(100)
        insertAllCourses(course)
    }

    @Update
    suspend fun updateLocalCourse(localUser: LocalCourses)

    @Query("DELETE FROM localCourses_table")
    suspend fun deleteLocalCourses()

    @Query("DELETE FROM localCourses_table WHERE idApi = :idApi")
    suspend fun deleteLocalCourseById(idApi: String)

    @Query("SELECT * FROM localCourses_table")
    suspend fun getCoursesInfo(): List<LocalCourses>

    @Query("SELECT * FROM localCourses_table")
    fun getCoursesInfoWithFlow(): Flow<List<LocalCourses>>

    @Query("SELECT * FROM localCourses_table WHERE idApi = :courseId")
    fun getCourseById(courseId: String): Flow<LocalCourses?>

    //Activities
    @Insert
    suspend fun insertLocalActivity(localActivity: LocalActivities)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLocalActivities(course: List<LocalActivities>)
    @Update
    suspend fun updateLocalActivity(localActivity: LocalActivities)

    @Query("DELETE FROM localActivities_table")
    suspend fun deleteLocalActivities()

    @Query("DELETE FROM localactivities_table WHERE idApi = :idApi")
    suspend fun deleteLocalActivityById(idApi: String)

    @Query("SELECT * FROM localActivities_table")
    suspend fun getActivitiesInfo(): List<LocalActivities>

    @Query("SELECT * FROM localActivities_table")
    fun getActivitiesInfoWithFlow(): Flow<List<LocalActivities>>

    @Query("SELECT * FROM localActivities_table WHERE idCourse = :courseId")
    fun getActivitiesInfoWithFlowByCourse(courseId: String): Flow<List<LocalActivities>>

    @Query("SELECT * FROM localActivities_table WHERE idApi = :activityId")
    fun getActivityById(activityId: String): Flow<LocalActivities>

    //Students
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStudent(student: LocalStudents)

    @Transaction
    suspend fun addStudentsToCourse(courseId: String, students: List<LocalStudents>) {
        students.forEach { student ->
            val existingStudent = getStudentByApiIdAndCourse(student.idApi, courseId)
            if (existingStudent == null) {
                insertOrUpdateStudent(student.copy(courseId = courseId))
            }
        }
    }

    @Query("SELECT * FROM localStudents_table WHERE idApi = :idApi AND courseId = :courseId")
    suspend fun getStudentByApiIdAndCourse(idApi: String, courseId: String): LocalStudents?

    @Query("SELECT * FROM localStudents_table WHERE courseId = :courseId")
    fun getStudentsByCourseId(courseId: String): Flow<List<LocalStudents>> // Return as Flow

    @Delete
    suspend fun deleteStudent(student: LocalStudents)

    // STUDENTS EVALUATIONS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateEvaluation(evaluation: LocalStudentEvaluation)

    @Query("SELECT * FROM localStudentEvaluation_table WHERE student_id = :studentId AND activity_id = :activityId")
    fun getEvaluationsForStudent(activityId: String, studentId: String): Flow<List<LocalStudentEvaluation>> // Return as Flow

    @Query("SELECT * FROM localStudentEvaluation_table WHERE student_id = :courseId")
    fun getAllEvaluationsForCourse(courseId: String): Flow<List<LocalStudentEvaluation>> // Return as Flow

    @Delete
    suspend fun deleteEvaluation(evaluation: LocalStudentEvaluation)

    // SUBMISSIONS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSubmission(submission: LocalActivitySubmission)

    @Query("SELECT * FROM localActivitySubmission_table WHERE idApi = :activityId AND student_id = :studentId")
    fun getSubmissionsForStudent(activityId: String, studentId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    @Query("SELECT * FROM localActivitySubmission_table WHERE student_id = :studentId AND course_id = :courseId")
    fun getSubmissionsForStudentAndCourse(studentId: String, courseId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    @Query("SELECT * FROM localActivitySubmission_table WHERE activity_id = :activityId")
    fun getAllSubmissionsForActivity(activityId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    @Delete
    suspend fun deleteSubmission(submission: LocalActivitySubmission)

    //Quizzes

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizz(quizz: QuizzEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionsEntity)

    @Transaction
    @Query("SELECT * FROM quizz WHERE course_id = :courseId AND id = :quizzId")
    fun getQuizzWithQuestions(courseId: String, quizzId: Int): Flow<QuizzWithQuestions>

    @Query("SELECT * FROM questions WHERE course_id = :courseId")
    fun getQuestionsForCourse(courseId: String): Flow<List<QuestionsEntity>>

    //SEEDERS

    // COURSES

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCourse(course: LocalCourses)

    // USERS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: LocalUser)



    // ACTIVITIES

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateActivity(activity: LocalActivities)

    // POSTS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePost(post: LocalPost)

    // QUIZZES

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateQuiz(quiz: QuizzEntity)

    // QUESTIONS

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateQuestion(question: QuestionsEntity)


}