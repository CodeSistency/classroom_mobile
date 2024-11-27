package com.example.classroom.presentation.screens.course.AddCourse

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.classroom.common.CustomButton.CustomButton
import com.example.classroom.common.CustomButton.NavigationButtonStyle
import com.example.classroom.common.CustomInput.CustomTextField
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.customSelect.CustomSelect
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun AddCourseScreenNew(
    id: String?,
    viewModel: AddCourseViewModel,
    coursesViewModel: CourseViewmodel,

    focusManager: FocusManager,
    navController: NavHostController,
){
    val scope = rememberCoroutineScope()
    val courseInfoState = viewModel.stateCourse.value
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }
//    var userInfo = coursesViewModel.userInfo.collectAsState(initial = null)
//
//    var state = coursesViewModel.stateCourseForm
//    var isPasswordOpen by remember { mutableStateOf(false) }

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

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        //Title input
                        // Campo de Token
//                        CustomTextField(
//                            value = viewModel.token.value,
//                            onValueChange = {
//                                viewModel.token.value = it
//                                viewModel.validateToken()
//                            },
//                            label = "Token",
//                            errorMessage = viewModel.tokenError.value ?: "",
//                            onNextClick = {
//                                focusManager.moveFocus(FocusDirection.Down)
//                            }
//                        )

                        // Campo de Título
                        CustomTextField(
                            value = viewModel.title.value,
                            onValueChange = {
                                viewModel.title.value = it
                                viewModel.validateTitle()
                            },
                            label = "Título",
                            errorMessage = viewModel.titleError.value ?: "",
                            onNextClick = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        // Campo de Descripción (opcional)
                        CustomTextField(
                            value = viewModel.description.value,
                            onValueChange = { viewModel.description.value = it },
                            label = "Descripción",
                            onNextClick = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        // Campo de ID del Propietario
//                        CustomTextField(
//                            value = viewModel.ownerId.value.toString(),
//                            onValueChange = {
//                                viewModel.ownerId.value = it.toIntOrNull() ?: 0
//                                viewModel.validateOwnerId()
//                            },
//                            label = "ID del Propietario",
//                            errorMessage = viewModel.ownerIdError.value ?: "",
//                            onNextClick = {
//                                focusManager.moveFocus(FocusDirection.Down)
//                            }
//                        )

                        // Campo de Nombre del Propietario
//                        CustomTextField(
//                            value = viewModel.ownerName.value,
//                            onValueChange = {
//                                viewModel.ownerName.value = it
//                                viewModel.validateOwnerName()
//                            },
//                            label = "Nombre del Propietario",
//                            errorMessage = viewModel.ownerNameError.value ?: "",
//                            onNextClick = {
//                                focusManager.moveFocus(FocusDirection.Down)
//                            }
//                        )

                        // Campo de Sección
                        CustomTextField(
                            value = viewModel.section.value,
                            onValueChange = {
                                viewModel.section.value = it
                                viewModel.validateSection()
                            },
                            label = "Sección",
                            errorMessage = viewModel.sectionError.value ?: "",
                            onNextClick = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        // Campo de Materia
                        CustomTextField(
                            value = viewModel.subject.value,
                            onValueChange = {
                                viewModel.subject.value = it
                                viewModel.validateSubject()
                            },
                            label = "Materia",
                            errorMessage = viewModel.subjectError.value ?: "",
                            onNextClick = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )

                        // Selección de Área
                        CustomSelect(
                            label = "Área",
                            options = Area.values().toList(),
                            selectedOption = listOf(viewModel.area.value),
                            onOptionSelected = { selected ->
                                if (selected.isNotEmpty()) viewModel.area.value = selected.first()
                                viewModel.validateArea()
                            },
                            multiple = false,
                            optionDisplay = { it.displayName }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón de Enviar
                        CustomButton(
                            text = if (id != null) "Actualizar curso" else "Crear Curso",
                            isLoading = courseInfoState.isLoading,
                            style = NavigationButtonStyle.SolidGradient,
                            color1 = Color(0xFF4CAF50),
                            color2 = Color(0xFF81C784),
                            onClick = {
                                scope.launch {
                                    viewModel.executeCourseRequest(id)

                                }
                            },
                            disabled = !viewModel.isFormValid || courseInfoState.isLoading
                        )

                    }
                }
            }
        }
    }
    LaunchedEffect(key1 = courseInfoState, block = {
        when{
            courseInfoState.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            courseInfoState.error != null -> {
                dialogState = SetupCustomDialogState.Error(courseInfoState.error.uiMessage)
            }

            else -> {
                if (courseInfoState.info != null){
                    dialogState = SetupCustomDialogState.Success(message = "El curso ha sido creado exitosamente")
                    delay(1000)
                    if (id != null){
                        navController.popBackStack()
                        navController.navigate(Destination.HOME.screenRoute){
                            popUpTo(Destination.REGISTRO_COURSE.screenRoute){
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                        viewModel.resetState()

                    }else{
                        navController.popBackStack()
                    }

                }
            }
        }
    })
    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}