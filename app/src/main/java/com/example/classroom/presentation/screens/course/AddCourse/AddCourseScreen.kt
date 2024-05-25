package com.example.classroom.presentation.screens.course.AddCourse

import android.annotation.SuppressLint
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
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddCourseScreen(
    id: String?,
    viewModel: CourseViewmodel,
    focusManager: FocusManager,
    navController: NavHostController,
){
    val scope = rememberCoroutineScope()
    val courseInfoState = viewModel.stateCourse.value
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }


    var isPasswordOpen by remember { mutableStateOf(false) }

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
                    Text(text = "Registro",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(vertical = 10.dp),
                    )
                }

                LazyColumn() {
                    item {
                        //Title input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_course_title_text),
                            darkTheme = false,
                            valueField = viewModel.titleField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.titleField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Description input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_course_descripcion_text),
                            darkTheme = false,
                            valueField = viewModel.descripcionField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.descripcionField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Email input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_course_seccion_text),
                            darkTheme = false,
                            valueField = viewModel.seccionField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.seccionField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Subject input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_course_subject_text),
                            darkTheme = false,
                            valueField = viewModel.subjectField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.subjectField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Area selector input


                        //Password input


                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.onCreateCourseClick(
                                        CourseRequestDto(
                                            area = viewModel.areaField.value,
                                            description = viewModel.descripcionField.value,
                                            owner = "",
                                            ownerName = "",
                                            section = viewModel.seccionField.value,
                                            subject = viewModel.subjectField.value,
                                            title = viewModel.titleField.value,
                                        ),
                                        null
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PaddingCustom.HORIZONTAL_STANDARD.size),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Azul
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !courseInfoState.isLoading
                        ) {
                            Text(
                                text = stringResource(id = R.string.register_course_boton_text), style = TextStyle(
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