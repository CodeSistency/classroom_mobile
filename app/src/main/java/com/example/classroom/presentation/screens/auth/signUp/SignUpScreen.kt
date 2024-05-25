package com.example.classroom.presentation.screens.auth.signUp

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.classroom.DateUtils
import com.example.classroom.R
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.common.datePicker.DatePickerWithDialog
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.domain.model.entity.Gender
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.SnackbarDelegate


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    focusManager: FocusManager,
    navController: NavHostController,
    darkTheme: Boolean,
){
    val scope = rememberCoroutineScope()
    val userInfo = viewModel.stateRegisterUser.value
    val userFormInfo = viewModel.stateRegisterForm
    val state = viewModel.stateRegisterForm


    LaunchedEffect(key1 = userInfo, block = {
        Log.e("form", userFormInfo.toString())
    })

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

//    val context = LocalContext.current
//    LaunchedEffect(key1 = context) {
//        viewModel.validationRegisterEvents.collect { event ->
//            when (event) {
//                AuthViewModel.ValidationEvent.Success -> viewModel.executeSignUp()
//            }
//        }
//    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)){
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
                        //Name input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_name_text),
                            darkTheme = darkTheme,
                            valueField =  state.name,
                            errorMsg = state.nameError,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.onSignUpEvent(SignUpFormEvent.NameChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Lastname input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_lastname_text),
                            darkTheme = darkTheme,
                            errorMsg = state.lastnameError,
                            valueField = state.lastname,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.onSignUpEvent(SignUpFormEvent.LastnameChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Email input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_correo_text),
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
                                viewModel.onSignUpEvent(SignUpFormEvent.EmailChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Phone input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_phone_text),
                            darkTheme = darkTheme,
                            errorMsg = state.phoneError,
                            valueField = state.phone,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.onSignUpEvent(SignUpFormEvent.PhoneChanged(it))
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))


//                        RadioButtonComponent(viewModel = viewModel)
                        Spacer(modifier = Modifier.padding(1.dp))

//                        val dateState = rememberDatePickerState()
//                        var isDatePickerOpen by remember { mutableStateOf(false) }
//                        val millisToLocalDate = dateState.selectedDateMillis?.let {
//                            DateUtils().convertMillisToLocalDate(it)
//                        }
//                        val dateToString = millisToLocalDate?.let {
//                            DateUtils().dateToString(millisToLocalDate)
//                        } ?: "Selecciona una fecha"
//
//
//                        Text(text = "Selecciona una fecha")
//                        Spacer(modifier = Modifier.padding(1.dp))
//                        Box(modifier = Modifier.padding(4.dp)){
//                            Button(onClick = { isDatePickerOpen = true }) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth()
//                                ) {
//                                    Text(text = dateToString)
//                                    Spacer(modifier = Modifier.weight(1f))
//                                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null)
//                                }
//                            }
//
//                        }
//                        if(isDatePickerOpen){
//                            DatePickerWithDialog(
//                                dateState = dateState,
//                                action = { },
//                                dismissDialog = { isDatePickerOpen = false}
//                            )
//                        }
//
//                        LaunchedEffect(key1 = dateToString, block = {
//                            viewModel.onInputRegisterChange("birthdate", dateToString)
//
//                        })

                        //Password input
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
                                viewModel.onSignUpEvent(SignUpFormEvent.PasswordChanged(it))
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
                                    viewModel.onSignUpEvent(SignUpFormEvent.Submit)

                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = PaddingCustom.HORIZONTAL_STANDARD.size)
                            ,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Azul
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
                    dialogState = SetupCustomDialogState.Success(message = "El usuario ha sido creado exitosamente")
                    delay(1000)
                    navController.popBackStack()
                    navController.navigate(Destination.LOGIN.screenRoute){
                        popUpTo(Destination.REGISTRO.screenRoute){
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

@Composable
fun RadioButtonComponent(viewModel: AuthViewModel) {
    val radioOptions = listOf("Man", "Woman", "Other")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var state = viewModel.stateRegisterForm
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row(
            Modifier,
            Arrangement.SpaceBetween
        ) {

            radioOptions.forEachIndexed { index, text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        .padding(horizontal = 16.dp)
                ) {

                    val context = LocalContext.current
                    RadioButton(
                        selected = (text == selectedOption),modifier = Modifier.padding(all = Dp(value = 8F)),
                        onClick = {
                            onOptionSelected(text)
                            state.gender = Gender.values()[index]
                            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                        }
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}