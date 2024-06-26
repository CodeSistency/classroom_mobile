package com.example.classroom.presentation.screens.activity.addActivity

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.classroom.DateUtils
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.datePicker.DatePickerWithDialog
import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.activity.addActivity.states.ActivityFormEvent
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.screens.auth.signUp.SignUpFormEvent
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddActivityScreen(
    idCourse: String,
    email: String,
    id: String?,
    viewModel: ActivityViewmodel,
    focusManager: FocusManager,
    navController: NavHostController,
){
    var state = viewModel.stateActivityForm
    val scope = rememberCoroutineScope()
    val activityInfoState = viewModel.stateAddActivity.value
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }
    val context = LocalContext.current
    val snackbarHost = remember { SnackbarHostState() }
    val snackbarDelegate = remember { SnackbarDelegate() }
    val scaffoldState = rememberScaffoldState()

    snackbarDelegate.apply {
        snackbarHostState = scaffoldState.snackbarHostState
        coroutineScope = scope
    }

    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHost)

            },
        ) {
            Column(Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.padding(top = 10.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        modifier = Modifier
                            .size(width = 70.dp, height = 70.dp)
                            .align(Alignment.Center)
                            .padding(vertical = 10.dp),
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "logo"
                    )

                }

                LazyColumn() {
                    item {
                        //Title input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_activity_title_text),
                            darkTheme = false,
                            valueField = state.title,
                            fieldRestriction = {
                                               it
//                                val withoutWhiteSpace = it.removeSuffix(" ")
//                                if (withoutWhiteSpace != "" || it.isEmpty()) {
//                                    withoutWhiteSpace
//                                } else {
//                                    null
//                                }
                            },
                            valueOnChange = {
                                viewModel.onActivityEvent(ActivityFormEvent.TitleChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Description input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_activity_descripcion_text),
                            darkTheme = false,
                            valueField = state.description,
                            fieldRestriction = {
                                               it
//                                val withoutWhiteSpace = it.removeSuffix(" ")
//                                if (withoutWhiteSpace != "" || it.isEmpty()) {
//                                    withoutWhiteSpace
//                                } else {
//                                    null
//                                }
                            },
                            valueOnChange = {
                                viewModel.onActivityEvent(ActivityFormEvent.DescriptionChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Grade input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_activity_grade_text),
                            darkTheme = false,
                            isNumberInput = true,
                            valueField = state.grade.toString() ?: "",
                            fieldRestriction = {
                                               it
//                                val withoutWhiteSpace = it.removeSuffix(" ")
//                                if (withoutWhiteSpace != "" || it.isEmpty()) {
//                                    withoutWhiteSpace
//                                } else {
//                                    null
//                                }
                            },
                            valueOnChange = {
                                if ( it.all { it.isDigit() }) {
                                    viewModel.onActivityEvent(ActivityFormEvent.GradeChanged(it))
                                }

                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Date input
                        val dateState = rememberDatePickerState()
                        val millisToLocalDate = dateState.selectedDateMillis?.let {
                            DateUtils().convertMillisToLocalDate(it)
                        }
                        val dateToString = millisToLocalDate?.let {
                            DateUtils().dateToString(millisToLocalDate)
                        } ?: "Selecciona una fecha"

                        LaunchedEffect(key1 = dateState.selectedDateMillis, block = {

                            if (millisToLocalDate != null){
                                viewModel.onActivityEvent(ActivityFormEvent.EndDateChanged(formatDate(millisToLocalDate)))
                            }else{
//                                Toast.makeText(context, "Intente de nuevo", Toast.LENGTH_SHORT).show()
                            }
                        })
                        var isOpenDatePicker by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.fillMaxWidth().padding(start = 24.dp)){
                            Button(onClick = { isOpenDatePicker = true }) {
                                Text(text = if (state.endDate.isBlank()) "Seleccione una fecha" else state.endDate)
                            }
                        }

                        if (isOpenDatePicker){
                            DatePickerWithDialog(
                                dateState = dateState,
                                action = {},
                                dismissDialog = { isOpenDatePicker = false}
                            )
                        }

                        Spacer(modifier = Modifier.padding(1.dp))


                        Button(
                            onClick = {
                                val currentDateFormatted = getCurrentDateInDesiredFormat()
                                viewModel.onActivityEvent(ActivityFormEvent.Submit(
                                    id,
                                    ActivityRequestDto(
                                        description = state.description,
                                        title = state.title,
                                        status = state.status,
                                        startDate = currentDateFormatted,
                                        endDate = state.endDate,
                                        idCourse = idCourse,
                                        grade = state.grade.toInt(),
                                        email = email
                                    )
                                    ))

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PaddingCustom.HORIZONTAL_STANDARD.size),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Azul
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !activityInfoState.isLoading
                        ) {
                            Text(
                                text = stringResource(id = R.string.register_activity_boton_text), style = TextStyle(
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
//                            fontFamily = InterTight
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}

// Function to get current date in DD-MM-AA format
@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDateInDesiredFormat(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")
    return currentDate.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yy")
    return date.format(formatter)
}