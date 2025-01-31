package com.example.classroom.data.local.db.daos

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
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.model.entity.LocalUser

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow


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
        deleteLocalPosts()
        deleteLocalStudents()
        deleteLocalMessages()
        deleteLocalPosts()
        deleteLocalChatRoom()
        deleteLocalActivitySubmissions()
        deleteLocalNotification()
        deleteLocalChatRoomUser()
    }

    @Query("DELETE FROM localActivitySubmission_table")
    suspend fun deleteLocalActivitySubmissions()

    @Query("DELETE FROM localPost_table")
    suspend fun deleteLocalPosts()
    @Query("DELETE FROM localStudents_table")
    suspend fun deleteLocalStudents()
    @Query("DELETE FROM localchatroom")
    suspend fun deleteLocalChatRoom()
    @Query("DELETE FROM localmessages")
    suspend fun deleteLocalMessages()
    @Query("DELETE FROM localnotification")
    suspend fun deleteLocalNotification()

    @Query("DELETE FROM localchatroomuser")
    suspend fun deleteLocalChatRoomUser()

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
    suspend fun insert(activity: LocalActivities)

    @Transaction
    suspend fun insertAllLocalActivities(course: List<LocalActivities>) {
        course.forEach { activity ->
            // Check if an activity with the same idApi and idCourse already exists
            val existingActivity = getActivityByApiAndCourse(activity.idApi, activity.idCourse)
            if (existingActivity == null) {
                // Insert only if it doesn't already exist
                insert(activity)
            }
        }
    }

    @Query("SELECT * FROM localActivities_table WHERE idApi = :idApi AND idCourse = :idCourse LIMIT 1")
    suspend fun getActivityByApiAndCourse(idApi: String, idCourse: String): LocalActivities?

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



    // SUBMISSIONS
    @Transaction
    suspend fun updateSubmissionTransaction(updatedSubmission: LocalActivitySubmission) {
        val existingSubmission = getSubmissionById(updatedSubmission.id)
        if (existingSubmission != null) {
            deleteSubmissionById(updatedSubmission.id) // Borra el anterior si existe
        }
        insertSubmission(updatedSubmission) // Inserta el nuevo registro
    }

    @Query("SELECT * FROM localActivitySubmission_table WHERE id = :id LIMIT 1")
    suspend fun getSubmissionById(id: Int): LocalActivitySubmission?

    @Query("DELETE FROM localActivitySubmission_table WHERE id = :id")
    suspend fun deleteSubmissionById(id: Int)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSubmission(submission: LocalActivitySubmission)

    @Query("SELECT * FROM localActivitySubmission_table WHERE activity_id = :activityId AND student_id = :studentId")
    fun getSubmissionsForStudent(activityId: String, studentId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    @Query("SELECT * FROM localActivitySubmission_table WHERE student_id = :studentId AND course_id = :courseId")
    fun getSubmissionsForStudentByCourse(studentId: String, courseId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    @Query(
        "SELECT * FROM localActivitySubmission_table " +
                "WHERE activity_id = :activityId AND student_id = :studentId"
    )
    suspend fun getSubmission(activityId: String, studentId: String): LocalActivitySubmission?

    // Insert a single submission
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: LocalActivitySubmission)

    // Insert a list of submissions (fallback for batch operations)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmissions(submissions: List<LocalActivitySubmission>)

    // Define a transaction for adding non-duplicate submissions
//    @Transaction
//    suspend fun addSubmissionsWithoutDuplicates(submissions: List<LocalActivitySubmission>) {
//        for (submission in submissions) {
//            // Check if the submission already exists
//            val existing = getSubmission(submission.activityId, submission.studentId)
//            if (existing == null) {
//                // Insert only if it doesn't already exist
//                insertSubmission(submission)
//            }
//        }
//    }


    @Query("SELECT * FROM localActivitySubmission_table WHERE activity_id = :activityId")
    fun getAllSubmissionsForActivity(activityId: String): Flow<List<LocalActivitySubmission>> // Return as Flow

    @Delete
    suspend fun deleteSubmission(submission: LocalActivitySubmission)



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

    @Query("DELETE FROM localActivitySubmission_table")
    suspend fun deleteAllSubmissions()



    @Transaction
    suspend fun addSubmissionsWithoutDuplicates(newSubmissions: List<LocalActivitySubmission>) {
        deleteAllSubmissions()
        insertSubmissions(newSubmissions)
    }
}