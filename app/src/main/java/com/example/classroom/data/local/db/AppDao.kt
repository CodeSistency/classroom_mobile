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
    }

    //Courses
    @Insert
    suspend fun insertLocalCourse(localCourse: LocalCourses)
    @Update
    suspend fun updateLocalCourse(localUser: LocalCourses)

    @Query("DELETE FROM localCourses_table")
    suspend fun deleteLocalCourses()

    @Query("SELECT * FROM localCourses_table")
    suspend fun getCoursesInfo(): List<LocalCourses>

    @Query("SELECT * FROM localCourses_table")
    fun getCoursesInfoWithFlow(): Flow<List<LocalCourses>>

    @Query("SELECT * FROM localCourses_table WHERE idApi = :courseId")
    fun getCourseById(courseId: Int): Flow<LocalCourses?>

    //Activities
    @Insert
    suspend fun insertLocalActivity(localCourse: LocalActivities)
    @Update
    suspend fun updateLocalActivity(localUser: LocalActivities)

    @Query("DELETE FROM localActivities_table")
    suspend fun deleteLocalActivity()

    @Query("SELECT * FROM localActivities_table")
    suspend fun getActivitiesInfo(): List<LocalActivities>

    @Query("SELECT * FROM localActivities_table")
    fun getAcivitiesInfoWithFlow(): Flow<List<LocalActivities>>

    @Query("SELECT * FROM localActivities_table WHERE idApi = :activityId")
    fun getActivityById(activityId: Int): Flow<LocalActivities?>

}