package com.example.classroom.presentation.screens.auth.signUp

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
import androidx.compose.material.Text
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
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    focusManager: FocusManager,
    navController: NavHostController,
    darkTheme: Boolean,
){
    val scope = rememberCoroutineScope()
    val userInfo = viewModel.stateLoginUser.value
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }


    var isPasswordOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()){
        Scaffold {
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
                            .align(Alignment.Center)
                            .padding(vertical = 10.dp),
                        )
                }

                LazyColumn() {
                    item {
                        //Name input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_name_text),
                            darkTheme = darkTheme,
                            valueField = viewModel.emailField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.emailField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Lastname input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_lastname_text),
                            darkTheme = darkTheme,
                            valueField = viewModel.lastnameField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.lastnameField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Email input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_correo_text),
                            darkTheme = darkTheme,
                            valueField = viewModel.emailField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.emailField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Phone input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_phone_text),
                            darkTheme = darkTheme,
                            valueField = viewModel.phoneField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.phoneField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Birthdate input
                        ItemInputField(
                            titulo = stringResource(id = R.string.register_birthdate_text),
                            darkTheme = darkTheme,
                            valueField = viewModel.birthdateField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.birthdateField.value = it
                            }
                        ) {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                        Spacer(modifier = Modifier.padding(1.dp))

                        //Password input
                        ItemInputField(
                            titulo = stringResource(id = R.string.login_clave_text),
                            darkTheme = darkTheme,
                            valueField = viewModel.passwordField.value,
                            fieldRestriction = {
                                val withoutWhiteSpace = it.removeSuffix(" ")
                                if (withoutWhiteSpace != "" || it.isEmpty()) {
                                    withoutWhiteSpace
                                } else {
                                    null
                                }
                            },
                            valueOnChange = {
                                viewModel.passwordField.value = it
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
                                    viewModel.onSignUpClick(
                                        SignUpRequestDto(
                                            email = viewModel.emailField.value,
                                            birthdate = viewModel.birthdateField.value,
                                            lastname = viewModel.lastnameField.value,
                                            name = viewModel.nameField.value,
                                            password = viewModel.passwordField.value,
                                            phone = viewModel.phoneField.value
                                        )
                                    )
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
                    }
                }
            }
        }
    }
}