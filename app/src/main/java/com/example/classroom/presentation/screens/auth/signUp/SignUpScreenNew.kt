package com.example.classroom.presentation.screens.auth.signUp

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.classroom.R
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.CustomDatePicker.CustomDatePicker
import com.example.classroom.common.composables.CustomInput.CustomTextField
import com.example.classroom.common.composables.CustomInput.ValidationRegex
import com.example.classroom.common.composables.FormWrapper.FormWrapper
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.composables.customSelect.CustomSelect
import com.example.classroom.common.composables.formScaffold.FormScaffold
import com.example.classroom.domain.model.entity.Gender
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreenNew(
    viewModel: AuthViewModel,
    focusManager: FocusManager,
    navController: NavHostController,
    darkTheme: Boolean
) {
    val scope = rememberCoroutineScope()
    val userInfo = viewModel.stateRegisterUser.value
    val userFormInfo = viewModel.stateRegisterForm
    var dialogState: SetupCustomDialogState by remember { mutableStateOf(SetupCustomDialogState.Default()) }

    // Handle form submission state
    LaunchedEffect(key1 = userInfo) {
        when {
            userInfo.isLoading -> dialogState = SetupCustomDialogState.Loading()
            userInfo.error != null -> dialogState = SetupCustomDialogState.Error(userInfo.error.uiMessage)
            else -> {
                userInfo.info?.let {
                    dialogState = SetupCustomDialogState.Success(message = "El usuario ha sido creado exitosamente")
                    delay(1000)
                    navController.navigate(Destination.LOGIN.screenRoute) {
                        popUpTo(Destination.REGISTRO.screenRoute) { inclusive = true }
                        launchSingleTop = true
                    }
                    viewModel.cleanData()
                }
            }
        }
    }

    // Main UI Layout
    FormScaffold(
        title = "Registrarse",
        subtitle = "Crea una cuenta",
        onBackClick = { navController.popBackStack() },
        primaryButton = {
            CustomButton(
                text = "Registrarse",
                style = NavigationButtonStyle.SolidGradient,
                color1 = Azul,
                color2 = AzulGradient,
                onClick = { scope.launch { viewModel.executeSignUpNew() } },
                disabled = !viewModel.isFormValid
            )
        },
        secondaryButton = null,
        content = {
            Column(Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.padding(top = 10.dp))

                LazyColumn {
                    item {

//                        // Logo Image
//                        LogoImage()

                        // Form Inputs
                        FormInputs(
                            viewModel = viewModel,
                            focusManager = focusManager
                        )


                    }
                }

            }
        }
    )

    // Custom Dialog to show loading or error states
    SetupCustomDialog(
        setupCustomDialogState = dialogState,
        showDialog = dialogState != SetupCustomDialogState.Default()
    ) {
        dialogState = SetupCustomDialogState.Default()
    }
}

// Logo Image Component
@Composable
fun LogoImage() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Image(
            modifier = Modifier.size(width = 70.dp, height = 70.dp)
                .align(Alignment.Center)
                .padding(vertical = 10.dp),
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "logo"
        )
    }
}

// Form Inputs (Reusable form fields)
@Composable
fun FormInputs(
    viewModel: AuthViewModel,
    focusManager: FocusManager
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // User Info Fields (Name, Lastname, Email, etc.)
        CustomTextField(
            value = viewModel.username.value,
            onValueChange = { viewModel.username.value = it; viewModel.validateUsername() },
            label = "Nombre de usuario",
            validationRegex = ValidationRegex.Alphanumeric,
            errorMessage = viewModel.nameError.value ?: "",
            onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(2.dp))

        CustomTextField(
            value = viewModel.name.value,
            onValueChange = { viewModel.name.value = it; viewModel.validateName() },
            label = "Nombre",
            validationRegex = ValidationRegex.Alphanumeric,
            errorMessage = viewModel.nameError.value ?: "",
            onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(2.dp))

        CustomTextField(
            value = viewModel.lastname.value,
            onValueChange = { viewModel.lastname.value = it; viewModel.validateLastname() },
            label = "Apellido",
            validationRegex = ValidationRegex.Alphanumeric,
            errorMessage = viewModel.lastnameError.value ?: "",
            onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(2.dp))

        CustomTextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it; viewModel.validatePassword() },
            label = "Contraseña",
            password = true,
            errorMessage = viewModel.passwordError.value ?: "",
            onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(2.dp))

        CustomTextField(
            value = viewModel.email.value,
            onValueChange = { viewModel.email.value = it; viewModel.validateEmail() },
            label = "Correo Electrónico",
            validationRegex = ValidationRegex.Email,
            errorMessage = viewModel.emailError.value ?: "",
            onNextClick = { focusManager.moveFocus(FocusDirection.Down) }
        )

        Spacer(modifier = Modifier.height(2.dp))

        CustomTextField(
            value = viewModel.phone.value,
            onValueChange = { viewModel.phone.value = it; viewModel.validatePhone() },
            label = "Teléfono",
            validationRegex = ValidationRegex.Phone,
            errorMessage = viewModel.phoneError.value ?: "",
            onNextClick = { focusManager.moveFocus(FocusDirection.Down) },
            showCountryCode = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomDatePicker(
            label = "Fecha de Nacimiento",
            selectedDate = viewModel.birthdate.value,
            onDateSelected = { viewModel.birthdate.value = it }
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Gender Picker
        CustomSelect(
            label = "Género",
            options = Gender.values().toList(),
            selectedOption = listOf(viewModel.gender.value),
            onOptionSelected = { selected ->
                if (selected.isNotEmpty()) viewModel.gender.value = selected.first()
            },
            multiple = false,
            optionDisplay = { it.displayName }
        )
    }
}

//@Composable
//fun RadioButtonComponent(viewModel: AuthViewModel) {
//    val radioOptions = listOf("Man", "Woman", "Other")
//    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
//    var state = viewModel.stateRegisterForm
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//
//        Row(
//            Modifier,
//            Arrangement.SpaceBetween
//        ) {
//
//            radioOptions.forEachIndexed { index, text ->
//                Row(
//                    Modifier
//                        .fillMaxWidth()
//                        .selectable(
//                            selected = (text == selectedOption),
//                            onClick = { onOptionSelected(text) }
//                        )
//                        .padding(horizontal = 16.dp)
//                ) {
//
//                    val context = LocalContext.current
//                    RadioButton(
//                        selected = (text == selectedOption),modifier = Modifier.padding(all = Dp(value = 8F)),
//                        onClick = {
//                            onOptionSelected(text)
//                            state.gender = Gender.values()[index]
//                            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
//                        }
//                    )
//                    Text(
//                        text = text,
//                        modifier = Modifier.padding(start = 16.dp)
//                    )
//                }
//            }
//        }
//    }
//}