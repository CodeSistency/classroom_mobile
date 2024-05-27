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
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
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
    @Query("SELECT * FROM localUser_table WHERE isLogged = 1 LIMIT 1")
    fun getLoggedInUser(): Flow<LocalUser?>

    // New method to get users by course ID
    @Query("SELECT * FROM localUser_table")
    fun getAllUsersWithFlow(): Flow<List<LocalUser>>

    fun getUsersByCourseIdWithFlow(courseId: String): Flow<List<LocalUser>> {
        return getAllUsersWithFlow()
            .map { users ->
                users.filter { user ->
                    val courses = UsersCoursesIdConverter().fromString(user.coursesId)
                    courses.contains(courseId)
                }
            }
    }

    @Transaction
    suspend fun updateUsersExcludingLoggedIn(users: List<LocalUser>, newCourseId: String) {
        // Fetch the logged-in user
        val loggedInUser = getLoggedInUser().firstOrNull()

        // Filter out the logged-in user from the list of users to be updated
        val usersToUpdate = users.filter { it.id != loggedInUser?.id }

        // Update the coursesId for each user in the list if the course ID is not already present
        val updatedUsers = usersToUpdate.map { user ->
            val courses = UsersCoursesIdConverter().fromString(user.coursesId)
            if (!courses.contains(newCourseId)) {
                user.copy(coursesId = UsersCoursesIdConverter().fromList(courses + newCourseId))
            } else {
                user
            }
        }

        // Insert or replace the filtered and updated users
        insertAllLocalUsers(updatedUsers)
    }

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

}