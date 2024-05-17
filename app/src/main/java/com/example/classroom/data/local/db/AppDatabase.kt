package proyecto.person.appconsultapopular.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.classroom.data.local.db.AppDao
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser


@Database(
    entities = [LocalUser::class, LocalCourses::class],
    version = 1,
    //autoMigrations = [AutoMigration(from = 1 , to = 2)],
    exportSchema = false
)

//@TypeConverters(
//    ProjectUserConverter::class, InfoProjectSelectConverter::class
//)

abstract class AppDatabase: RoomDatabase() {
    abstract val appDao: AppDao

    companion object {
        const val DATABASE_NAME = "app_classroom"
    }
}