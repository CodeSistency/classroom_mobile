package com.example.classroom.presentation.screens.auth.signIn

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.classroom.R
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.CustomInput.CustomTextField
import com.example.classroom.common.composables.CustomInput.ValidationRegex
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInScreenNew(
    viewModel: SignInViewModel,
    focusManager: FocusManager,
    navController: NavHostController,
    darkTheme: Boolean,
){
    val scope = rememberCoroutineScope()
    val userInfo = viewModel.stateLoginUser.value

    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }

    val snackbarHost = remember { SnackbarHostState() }
    val snackbarDelegate = remember { SnackbarDelegate() }
    val scaffoldState = rememberScaffoldState()

    snackbarDelegate.apply {
        snackbarHostState = scaffoldState.snackbarHostState
        coroutineScope = scope
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Top white section with logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.35f)
                .background(Color.White)
                .align(Alignment.TopCenter)
        ) {
            Image(
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.Center)
                    .padding(vertical = 20.dp),
                painter = painterResource(id = R.drawable.ic_logo), // Replace with your logo
                contentDescription = "logo"
            )
        }

        // Bottom blue section with rounded corners and login form
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .background(Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .align(Alignment.BottomCenter)
        ) {
            // Login Form content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                // Email Input
                CustomTextField(
                    value = viewModel.email.value,
                    onValueChange = {
                        viewModel.email.value = it
                        viewModel.validateEmail()
                    },
                    label = "Correo Electrónico",
                    validationRegex = ValidationRegex.Email,
                    errorMessage = viewModel.emailError.value ?: "",
                    onNextClick = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                // Password Input
                CustomTextField(
                    value = viewModel.password.value,
                    onValueChange = {
                        viewModel.password.value = it
                        viewModel.validatePassword()
                    },
                    label = "Contraseña",
                    password = true, // Enable password visibility toggle
                    errorMessage = viewModel.passwordError.value ?: "",
                    onNextClick = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                CustomButton(
                    text = "Iniciar Sesión",
                    style = NavigationButtonStyle.SolidGradient,
                    color1 = Azul,
                    color2 = AzulGradient,
                    onClick = {
                        scope.launch {
                            viewModel.executeSignIn()
                        }
                    },
                    disabled = !viewModel.isFormValid
                )

                Spacer(modifier = Modifier.height(15.dp))

                // Registration Link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "¿No tienes una cuenta? Registrate",
                        modifier = Modifier.clickable {
                            navController.navigate(Destination.REGISTRO.screenRoute)
                            viewModel.cleanData()
                        }
                    )
                }
            }
        }
    }

    // Handle login events and navigation
    LaunchedEffect(key1 = userInfo) {
        when {
            userInfo.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            userInfo.error != null -> {
                dialogState = SetupCustomDialogState.Error(userInfo.error.uiMessage)
            }
            else -> {
                if (userInfo.info != null) {
                    dialogState = SetupCustomDialogState.Default()
                    navController.navigate(Destination.HOME.screenRoute) {
                        popUpTo(Destination.LOGIN.screenRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    viewModel.cleanData()
                }
            }
        }
    }

    // Show dialog based on login state
    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun SignInScreenNew(
//    viewModel: SignInViewModel,
//    focusManager: FocusManager,
//    navController: NavHostController,
//    darkTheme: Boolean,
//){
//    val scope = rememberCoroutineScope()
//    val userInfo = viewModel.stateLoginUser.value
//
//    var dialogState: SetupCustomDialogState by remember {
//        mutableStateOf(SetupCustomDialogState.Default())
//    }
//
//    val snackbarHost = remember { SnackbarHostState() }
//    val snackbarDelegate = remember { SnackbarDelegate() }
//    val scaffoldState = rememberScaffoldState()
//
//    snackbarDelegate.apply {
//        snackbarHostState = scaffoldState.snackbarHostState
//        coroutineScope = scope
//    }
//
//
//
////    val context = LocalContext.current
////    LaunchedEffect(key1 = context) {
////        viewModel.validationLoginEvents.collect { event ->
////            when (event) {
////                AuthViewModel.ValidationEvent.Success -> viewModel.executeSignIn()
////            }
////        }
////    }
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.White)){
//        Scaffold(
//            scaffoldState = scaffoldState,
//            snackbarHost = {
//                SnackbarHost(hostState = snackbarHost)
//            },
//        ) {
//            FormWrapper2 {
//                item {
//                    Column(Modifier.fillMaxSize()) {
//                        Spacer(modifier = Modifier.padding(top = 10.dp))
//                        Box(modifier = Modifier.fillMaxWidth()) {
//                            Image(
//                                modifier = Modifier
//                                    .size(width = 200.dp, height = 200.dp)
//                                    .align(Alignment.Center)
//                                    .padding(vertical = 20.dp),
//                                painter = painterResource(id = R.drawable.ic_logo),
//                                contentDescription = "logo"
//                            )
//
//                        }
//                        Column(   modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                            verticalArrangement = Arrangement.spacedBy(8.dp)) {
//
//                                // Email Input
//                                CustomTextField(
//                                    value = viewModel.email.value,
//                                    onValueChange = {
//                                        viewModel.email.value = it
//                                        viewModel.validateEmail()
//                                    },
//                                    label = "Correo Electrónico",
//                                    validationRegex = ValidationRegex.Email,
//                                    errorMessage = viewModel.emailError.value ?: "",
//                                    onNextClick = {
//                                        focusManager.moveFocus(FocusDirection.Down)
//                                    }
//                                )
//
//                                // Password Input
//                                CustomTextField(
//                                    value = viewModel.password.value,
//                                    onValueChange = {
//                                        viewModel.password.value = it
//                                        viewModel.validatePassword()
//                                    },
//                                    label = "Contraseña",
//                                    password = true, // Enable password visibility toggle
//                                    errorMessage = viewModel.passwordError.value ?: "",
//                                    onNextClick = {
//                                        focusManager.moveFocus(FocusDirection.Down)
//                                    }
//                                )
//
//                                Spacer(modifier = Modifier.height(16.dp))
//
//                                // Submit Button
//                                CustomButton(
//                                    text = "Iniciar Sesión",
//                                    style = NavigationButtonStyle.SolidGradient,
//                                    color1 = Azul,
//                                    color2 = AzulGradient,
//                                    onClick = {
//                                        scope.launch {
//                                            viewModel.executeSignIn()
//                                        }
//                                        // Trigger sign-in execution
//                                    },
//                                    disabled = !viewModel.isFormValid
//                                )
////                        Box(modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp)).fillMaxWidth().height(4.dp))
//
//                                Spacer(modifier = Modifier.height(15.dp))
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    Arrangement.Center,
//                                ) {
//                                    Text(text = "¿No tienes una cuenta? Registrate",
//                                        modifier= Modifier.clickable{
//                                            navController.navigate(Destination.REGISTRO.screenRoute)
//                                            viewModel.cleanData()
//                                        })
//                                }
//
//                        }
//                    }
//
//                }
//            }
////            FormWrapper {
////                Column(Modifier.fillMaxSize()) {
////                    Spacer(modifier = Modifier.padding(top = 10.dp))
////                    Box(modifier = Modifier.fillMaxWidth()) {
////                        Image(
////                            modifier = Modifier
////                                .size(width = 200.dp, height = 200.dp)
////                                .align(Alignment.Center)
////                                .padding(vertical = 20.dp),
////                            painter = painterResource(id = R.drawable.ic_logo),
////                            contentDescription = "logo"
////                        )
////
////                    }
////                    LazyColumn(   modifier = Modifier
////                        .fillMaxWidth()
////                        .padding(16.dp),
////                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
////                        item {
////                            // Email Input
////                            CustomTextField(
////                                value = viewModel.email.value,
////                                onValueChange = {
////                                    viewModel.email.value = it
////                                    viewModel.validateEmail()
////                                },
////                                label = "Correo Electrónico",
////                                validationRegex = ValidationRegex.Email,
////                                errorMessage = viewModel.emailError.value ?: "",
////                                onNextClick = {
////                                    focusManager.moveFocus(FocusDirection.Down)
////                                }
////                            )
////
////                            // Password Input
////                            CustomTextField(
////                                value = viewModel.password.value,
////                                onValueChange = {
////                                    viewModel.password.value = it
////                                    viewModel.validatePassword()
////                                },
////                                label = "Contraseña",
////                                password = true, // Enable password visibility toggle
////                                errorMessage = viewModel.passwordError.value ?: "",
////                                onNextClick = {
////                                    focusManager.moveFocus(FocusDirection.Down)
////                                }
////                            )
////
////                            Spacer(modifier = Modifier.height(16.dp))
////
////                            // Submit Button
////                            CustomButton(
////                                text = "Iniciar Sesión",
////                                style = NavigationButtonStyle.SolidGradient,
////                                color1 = Azul,
////                                color2 = AzulGradient,
////                                onClick = {
////                                    scope.launch {
////                                        viewModel.executeSignIn()
////                                    }
////                                    // Trigger sign-in execution
////                                },
////                                disabled = !viewModel.isFormValid
////                            )
//////                        Box(modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp)).fillMaxWidth().height(4.dp))
////
////                            Spacer(modifier = Modifier.height(15.dp))
////                            Row(
////                                modifier = Modifier.fillMaxWidth(),
////                                Arrangement.Center,
////                            ) {
////                                Text(text = "¿No tienes una cuenta? Registrate",
////                                    modifier= Modifier.clickable{
////                                        navController.navigate(Destination.REGISTRO.screenRoute)
////                                    })
////                            }
////                        }
////                    }
////                }
////
////            }
////            Column(Modifier.fillMaxSize()) {
////                Spacer(modifier = Modifier.padding(top = 10.dp))
////                Box(modifier = Modifier.fillMaxWidth()) {
////                    Image(
////                        modifier = Modifier
////                            .size(width = 200.dp, height = 200.dp)
////                            .align(Alignment.Center)
////                            .padding(vertical = 20.dp),
////                        painter = painterResource(id = R.drawable.ic_logo),
////                        contentDescription = "logo"
////                    )
////
////                }
////                LazyColumn(   modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(16.dp),
////                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
////                    item {
////                        // Email Input
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
////
////                        // Password Input
////                        CustomTextField(
////                            value = viewModel.password.value,
////                            onValueChange = {
////                                viewModel.password.value = it
////                                viewModel.validatePassword()
////                            },
////                            label = "Contraseña",
////                            password = true, // Enable password visibility toggle
////                            errorMessage = viewModel.passwordError.value ?: "",
////                            onNextClick = {
////                                focusManager.moveFocus(FocusDirection.Down)
////                            }
////                        )
////
////                        Spacer(modifier = Modifier.height(16.dp))
////
////                        // Submit Button
////                        CustomButton(
////                            text = "Iniciar Sesión",
////                            style = NavigationButtonStyle.SolidGradient,
////                            color1 = Color(0xFF4CAF50),
////                            color2 = Color(0xFF81C784),
////                            onClick = {
////                                scope.launch {
////                                    viewModel.executeSignIn()
////                                }
////                                // Trigger sign-in execution
////                            },
////                            disabled = !viewModel.isFormValid
////                        )
//////                        Box(modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp)).fillMaxWidth().height(4.dp))
////
////                        Spacer(modifier = Modifier.height(15.dp))
////                        Row(
////                            modifier = Modifier.fillMaxWidth(),
////                            Arrangement.Center,
////                        ) {
////                            Text(text = "¿No tienes una cuenta? Registrate",
////                                modifier= Modifier.clickable{
////                                    navController.navigate(Destination.REGISTRO.screenRoute)
////                                })
////                        }
////                    }
////                }
////            }
//        }
//    }
//
//    LaunchedEffect(key1 = userInfo, block = {
//        when{
//            userInfo.isLoading -> {
//                dialogState = SetupCustomDialogState.Loading()
//            }
//            userInfo.error != null -> {
//                dialogState = SetupCustomDialogState.Error(userInfo.error.uiMessage)
//            }
//            else -> {
//                if (userInfo.info != null){
//                    dialogState = SetupCustomDialogState.Default()
////                    navController.popBackStack()
//                    navController.navigate(Destination.HOME.screenRoute){
//                        popUpTo(Destination.LOGIN.screenRoute){
//                            inclusive = true
//                        }
//                        launchSingleTop = true
//                    }
//                    viewModel.cleanData()
//
////                    viewModel.cleanInfo()
//                }
//            }
//        }
//    })
//    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
//        dialogState = SetupCustomDialogState.Default()
//    }
//}