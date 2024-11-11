package proyecto.person.appconsultapopular.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.data.local.db.LocalPostDao
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.domain.model.entity.LocalStudentEvaluation
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.QuestionsEntity
import com.example.classroom.domain.model.entity.QuizzEntity
import com.example.classroom.domain.model.typeConverter.IntListTypeConverter
import com.example.classroom.domain.model.typeConverter.UsersCoursesIdConverter
import com.example.classroom.domain.model.typeConverter.UsersInCourseConverter


@Database(
    entities = [LocalUser::class, LocalCourses::class, LocalActivities::class, LocalStudents::class,
        QuestionsEntity::class, QuizzEntity::class, LocalActivitySubmission::class, LocalStudentEvaluation::class, LocalPost::class],
    version = 1,
    //autoMigrations = [AutoMigration(from = 1 , to = 2)],
    exportSchema = false
)

@TypeConverters(
    UsersInCourseConverter::class, UsersCoursesIdConverter::class, IntListTypeConverter::class
)

abstract class AppDatabase: RoomDatabase() {
    abstract val appDao: AppDao
    abstract val localPostDao: LocalPostDao

    companion object {
        const val DATABASE_NAME = "app_classroom"
    }
}