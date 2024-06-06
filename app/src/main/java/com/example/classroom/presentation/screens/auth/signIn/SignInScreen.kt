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
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
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
import androidx.compose.ui.draw.shadow
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
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    viewModel: AuthViewModel,
    focusManager: FocusManager,
    navController: NavHostController,
    darkTheme: Boolean,
){
    val scope = rememberCoroutineScope()
    val userInfo = viewModel.stateLoginUser.value
    val state = viewModel.stateLoginForm

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

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordOpen by remember { mutableStateOf(false) }

//    val context = LocalContext.current
//    LaunchedEffect(key1 = context) {
//        viewModel.validationLoginEvents.collect { event ->
//            when (event) {
//                AuthViewModel.ValidationEvent.Success -> viewModel.executeSignIn()
//            }
//        }
//    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)){
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
                            .size(width = 200.dp, height = 200.dp)
                            .align(Alignment.Center)
                            .padding(vertical = 20.dp),
                        painter = painterResource(id = R.drawable.ic_logo),
                        contentDescription = "logo"
                    )

                }
                LazyColumn() {
                    item {
                        ItemInputField(
                            titulo = stringResource(id = R.string.login_correo_text),
                            darkTheme = darkTheme,
                            errorMsg = state.emailError,
                            valueField = state.email,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.onSignInEvent(SignInFormEvent.EmailChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }

                        Spacer(modifier = Modifier.padding(1.dp))

                        ItemInputField(
                            titulo = stringResource(id = R.string.login_clave_text),
                            darkTheme = darkTheme,
                            errorMsg = state.passwordError,
                            valueField = state.password,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.onSignInEvent(SignInFormEvent.PasswordChanged(it))
                            },
                            isPassword = true,
                            showPassword = isPasswordOpen,
                            changePasswordValue = {
                                isPasswordOpen = !isPasswordOpen
                            }
                        ) {
                            focusManager.clearFocus(true)
                        }

                        Spacer(modifier = Modifier.padding(3.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.onSignInEvent(SignInFormEvent.Submit)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PaddingCustom.HORIZONTAL_STANDARD.size),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !userInfo.isLoading
                        ) {
                            Text(
                                text = stringResource(id = R.string.login_boton_text), style = TextStyle(
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
//                            fontFamily = InterTight
                                )
                            )
                        }
//                        Box(modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp)).fillMaxWidth().height(4.dp))

                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            Arrangement.Center,
                        ) {
                            Text(text = "¿No tienes una cuenta? Registrate",
                                modifier= Modifier.clickable{
                                    navController.navigate(Destination.REGISTRO.screenRoute)
                                })
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = userInfo, block = {
        when{
            userInfo.isLoading -> {
                dialogState = SetupCustomDialogState.Loading()
            }
            userInfo.error != null -> {
                dialogState = SetupCustomDialogState.Error(userInfo.error.uiMessage)
            }
            else -> {
                if (userInfo.info != null){
                    dialogState = SetupCustomDialogState.Default()
                    navController.popBackStack()
                    navController.navigate(Destination.HOME.screenRoute){
                        popUpTo(Destination.LOGIN.screenRoute){
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    })
    SetupCustomDialog(setupCustomDialogState = dialogState, showDialog = dialogState != SetupCustomDialogState.Default()) {
        dialogState = SetupCustomDialogState.Default()
    }
}