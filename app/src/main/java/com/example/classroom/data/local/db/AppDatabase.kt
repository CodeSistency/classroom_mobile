package proyecto.person.appconsultapopular.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.typeConverter.UsersCoursesIdConverter
import com.example.classroom.domain.model.typeConverter.UsersInCourseConverter


@Database(
    entities = [LocalUser::class, LocalCourses::class, LocalActivities::class],
    version = 1,
    //autoMigrations = [AutoMigration(from = 1 , to = 2)],
    exportSchema = false
)

@TypeConverters(
    UsersInCourseConverter::class, UsersCoursesIdConverter::class
)

abstract class AppDatabase: RoomDatabase() {
    abstract val appDao: AppDao

    companion object {
        const val DATABASE_NAME = "app_classroom"
    }
}