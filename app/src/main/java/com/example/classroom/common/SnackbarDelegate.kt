package proyecto.person.appconsultapopular.common

import android.content.Context
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class SnackbarState {
    DEFAULT,
    ERROR,
    SUCCESS
}

class SnackbarDelegate(
    var snackbarHostState: SnackbarHostState? = null,
    var coroutineScope: CoroutineScope? = null
) {

    private var snackbarState: SnackbarState = SnackbarState.DEFAULT

    val snackbarBackgroundColor: Color
        @Composable
        get() = when (snackbarState) {
            else -> {
                val context = LocalContext.current
                val manufacturer = Build.MANUFACTURER

                if (manufacturer == "Xiaomi"){
                    if (isSystemInDarkTheme()){
                        Color.Gray
                    } else SnackbarDefaults.backgroundColor
                } else SnackbarDefaults.backgroundColor

            }
        }


    fun showSnackbar(
        state: SnackbarState,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
    ) {


        this.snackbarState = state
        coroutineScope?.launch {
            snackbarHostState?.showSnackbar(message, actionLabel, duration)
        }
    }
}