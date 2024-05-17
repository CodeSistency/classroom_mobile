package proyecto.person.appconsultapopular.common


import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import com.example.classroom.App
import com.example.classroom.R

import java.io.File

object AppResource {
    fun getString(@StringRes stringRes: Int): String {
        return App.instance.resources.getString(stringRes)
    }

    fun getBoolean(@BoolRes boolRes: Int): Boolean {
        return App.instance.resources.getBoolean(boolRes)
    }

    fun getDrawable(@DrawableRes res: Int): Drawable{
        return App.instance.resources.getDrawable(res)
    }

    fun getInt(@IntegerRes intRes: Int): Int {
        return App.instance.resources.getInteger(intRes)
    }

    fun getAppContext(): Context {
        return App.instance.applicationContext
    }

    fun getAppInstance(): App {
        return App.instance
    }

    fun getIsUserLogged(): Boolean?{
        return App.isUserLogged
    }

    fun getDarkTheme(): Boolean?{
        return App.isDarkTheme
    }

    //Store the capture image
    fun getDirectory(): File {
        val mediaDir = App.instance.externalMediaDirs.firstOrNull()?.let {
            File(it, App.instance.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else App.instance.filesDir
    }

    fun getStringArray(@ArrayRes arrayRes: Int): Array<out String> {
        return App.instance.resources.getStringArray(arrayRes)
    }

    fun setDarkTheme(value: Boolean){
        App.isDarkTheme = value
    }



}