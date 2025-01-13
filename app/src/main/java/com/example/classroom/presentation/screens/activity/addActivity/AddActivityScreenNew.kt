package com.example.classroom.presentation.screens.activity.addActivity

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.Card
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Brush
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
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.CustomDatePicker.CustomDatePicker
import com.example.classroom.common.composables.CustomInput.CustomTextField
import com.example.classroom.common.composables.CustomInput.ValidationRegex
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.composables.customSelect.CustomSelect
import com.example.classroom.common.composables.datePicker.DatePickerWithDialog
import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.activity.addActivity.states.ActivityFormEvent
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.screens.auth.signUp.SignUpFormEvent
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddActivityScreenNew(
    idCourse: String,
    id: String?,
    viewModel: AddActivityViewModel,
    focusManager: FocusManager,
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val activityInfoState = viewModel.stateAddActivity.collectAsState()
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = remember { SnackbarHostState() })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header with Icon and Title
            Image(
                modifier = Modifier
                    .size(90.dp)
                    .padding(8.dp),
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "logo"
            )
            Text(
                text = "Registrar Actividad",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Fields Section with Padding and Spacing
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                CustomTextField(
                    value = viewModel.description.value,
                    onValueChange = { viewModel.description.value = it },
                    label = "Descripción",
                    onNextClick = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CustomDatePicker(
                        label = "Fecha de Inicio",
                        selectedDate = viewModel.startDate.value,
                        onDateSelected = {
                            viewModel.startDate.value = it
                            viewModel.validateStartDate()
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CustomDatePicker(
                        label = "Fecha de Finalización",
                        selectedDate = viewModel.endDate.value,
                        onDateSelected = {
                            viewModel.endDate.value = it
                            viewModel.validateEndDate()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                if (id == null) {
                    CustomSelect(
                        label = "Status",
                        options = Status.values().toList(),
                        selectedOption = listOf(viewModel.status.value),
                        onOptionSelected = { selected ->
                            if (selected.isNotEmpty()) viewModel.status.value = selected.first()
                            viewModel.validateStatus()
                        },
                        multiple = false,
                        optionDisplay = { it.displayName }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Button Section
            CustomButton(
                text = "Crear Actividad",
                style = NavigationButtonStyle.SolidGradient,
                color1 = MaterialTheme.colors.primary,
                color2 = MaterialTheme.colors.primaryVariant,
                onClick = {
                    scope.launch {
                        viewModel.executeActivityRequest(id, idCourse)
                    }
                },
                disabled = !viewModel.isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }

    LaunchedEffect(key1 = activityInfoState.value) {
        when {
            activityInfoState.value.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            activityInfoState.value.error != null -> {
                dialogState = SetupCustomDialogState.Error(activityInfoState.value.error?.uiMessage)
            }
            else -> {
                if (activityInfoState.value.info != null) {
                    dialogState = SetupCustomDialogState.Success(message = "Se ha creado la actividad exitosamente")
                    delay(1000)
                    navController.popBackStack()
                    viewModel.resetForm()
                }
            }
        }
    }

    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}
//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun AddActivityScreenNew(
//    idCourse: String,
//    id: String?,
//    viewModel: AddActivityViewModel,
//    focusManager: FocusManager,
//    navController: NavHostController,
//){
//    val scope = rememberCoroutineScope()
//    val activityInfoState = viewModel.stateAddActivity.collectAsState()
//    var dialogState: SetupCustomDialogState by remember {
//        mutableStateOf(SetupCustomDialogState.Default())
//    }
//    val context = LocalContext.current
//    val snackbarHost = remember { SnackbarHostState() }
//    val snackbarDelegate = remember { SnackbarDelegate() }
//    val scaffoldState = rememberScaffoldState()
//
//    snackbarDelegate.apply {
//        snackbarHostState = scaffoldState.snackbarHostState
//        coroutineScope = scope
//    }
//
//    Box(modifier = Modifier.fillMaxSize()){
//        Scaffold(
//            scaffoldState = scaffoldState,
//            snackbarHost = {
//                SnackbarHost(hostState = snackbarHost)
//
//            },
//        ) {
//            Column(Modifier.fillMaxSize()) {
//                Spacer(modifier = Modifier.padding(top = 10.dp))
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    Image(
//                        modifier = Modifier
//                            .size(width = 70.dp, height = 70.dp)
//                            .align(Alignment.Center)
//                            .padding(vertical = 10.dp),
//                        painter = painterResource(id = R.drawable.ic_logo),
//                        contentDescription = "logo"
//                    )
//
//                }
//
//                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    item {
//                        // Campo de Título
//                        CustomTextField(
//                            value = viewModel.title.value,
//                            onValueChange = {
//                                viewModel.title.value = it
//                                viewModel.validateTitle()
//                            },
//                            label = "Título",
//                            errorMessage = viewModel.titleError.value ?: "",
//                            onNextClick = {
//                                focusManager.moveFocus(FocusDirection.Down)
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//
//                        // Campo de Descripción (opcional)
//                        CustomTextField(
//                            value = viewModel.description.value,
//                            onValueChange = { viewModel.description.value = it },
//                            label = "Descripción",
//                            onNextClick = {
//                                focusManager.moveFocus(FocusDirection.Down)
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//
//
//                        // Campo de Calificación
////                        CustomTextField(
////                            value = viewModel.grade.value.toString(),
////                            onValueChange = {
////                                viewModel.grade.value = it.toIntOrNull() ?: 0
////                                viewModel.validateGrade()
////                            },
////                            label = "Calificación",
////                            errorMessage = viewModel.gradeError.value ?: "",
////                            onNextClick = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            }
////                        )
//
//                        // Campo de Correo Electrónico
////                        CustomTextField(
////                            value = viewModel.email.value,
////                            onValueChange = {
////                                viewModel.email.value = it
////                                viewModel.validateEmail()
////                            },
////                            label = "Correo Electrónico",
////                            validationRegex = ValidationRegex.Email,
////                            errorMessage = viewModel.emailError.value ?: "",
////                            onNextClick = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            }
////                        )
//
//                        // Fecha de Inicio
//                        CustomDatePicker(
//                            label = "Fecha de Inicio",
//                            selectedDate = viewModel.startDate.value,
//                            onDateSelected = {
//                                viewModel.startDate.value = it
//                                viewModel.validateStartDate()
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//
//                        // Fecha de Finalización
//                        CustomDatePicker(
//                            label = "Fecha de Finalización",
//                            selectedDate = viewModel.endDate.value,
//                            onDateSelected = {
//                                viewModel.endDate.value = it
//                                viewModel.validateEndDate()
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//
//                        // Selección de Estado
//                        if (id == null){
//                            CustomSelect(
//                                label = "Status",
//                                options = Status.values().toList(),
//                                selectedOption = listOf(viewModel.status.value),
//                                onOptionSelected = { selected ->
//                                    if (selected.isNotEmpty()) viewModel.status.value = selected.first()
//                                    viewModel.validateStatus()
//                                },
//                                multiple = false,
//                                optionDisplay = { it.displayName }
//                            )
//                        }
//
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Botón de Enviar
//                        CustomButton(
//                            text = "Crear Actividad",
//                            style = NavigationButtonStyle.SolidGradient,
//                            color1 = Color(0xFF4CAF50),
//                            color2 = Color(0xFF81C784),
//                            onClick = {
//                                scope.launch {
//                                    viewModel.executeActivityRequest(id,idCourse)
//                                }
//                            },
//                            disabled = !viewModel.isFormValid
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    LaunchedEffect(key1 = activityInfoState.value, block = {
//        when{
//            activityInfoState.value.isLoading -> {
//                dialogState = SetupCustomDialogState.Loading()
//            }
//            activityInfoState.value.error != null -> {
//                dialogState = SetupCustomDialogState.Error(activityInfoState.value.error?.uiMessage)
//            }
//
//            else -> {
//                if (activityInfoState.value.info != null){
//                    dialogState = SetupCustomDialogState.Success(message = "Se ha creado la actividad exitosamente")
//                    delay(1000)
//                    navController.popBackStack()
//                    viewModel.resetForm()
//                }
//            }
//        }
//    })
//
//    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
//        dialogState = SetupCustomDialogState.Default()
//    }
//}

// Function to get current date in DD-MM-AA format
