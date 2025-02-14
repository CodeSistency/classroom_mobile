package com.example.classroom.presentation.screens.home.composables

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.common.composables.CustomButton.CustomButton
import com.example.classroom.common.composables.CustomButton.NavigationButtonStyle
import com.example.classroom.common.composables.customDialogs.SetupCustomDialog
import com.example.classroom.common.composables.customDialogs.SetupCustomDialogState
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.screens.home.HomeViewmodel
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SelectedOptionDialog(
    dismissDialog: () -> Unit,
    navController: NavController,
    viewmodel: HomeViewmodel,
    scope: CoroutineScope
) {
    val state = viewmodel.stateJoinCourse.value
    val user = viewmodel.userInfo.collectAsState(initial = null)
    var dialogState: SetupCustomDialogState by remember {
        mutableStateOf(SetupCustomDialogState.Default())
    }
    Dialog(
        onDismissRequest = dismissDialog,
    ) {
        val context = LocalContext.current
        var isSelectedOption by remember {
            mutableStateOf(Options.NO_SELECTED)
        }
        var input by remember {
            mutableStateOf("")
        }
        Box(modifier = Modifier.fillMaxWidth()){
            var position =  Modifier.align(Alignment.TopStart)
            Column(
                modifier = Modifier
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when(isSelectedOption){
                    Options.NO_SELECTED -> {
                        Text(text = "Selecciona una opción",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
//                        fontFamily = InterTight,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(horizontal = 5.dp,)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        user.value?.let {
                            if (it.rol == 2){

                                // Profesor
                                CustomButton(
                                    onClick = {
                                        navController.navigate(Destination.REGISTRO_COURSE.screenRoute)
                                    },
                                    text = "Crear a una clase",
                                    style = NavigationButtonStyle.OutlineWithIconGradient,
                                    color1 = Azul,
                                    color2 = AzulGradient,
                                    icon = Icons.Default.ArrowForwardIos,
                                    modifier = Modifier.fillMaxWidth())

                            }else if (it.rol == 1){

                                // Estudiante

                                CustomButton(
                                    onClick = {
                                        isSelectedOption = Options.JOIN_CLASS
                                    },
                                    text = "Unirse a una clase",
                                    style = NavigationButtonStyle.OutlineWithIconGradient,
                                    color1 = Azul,
                                    color2 = AzulGradient,
                                    icon = Icons.Default.ArrowForwardIos,
                                    modifier = Modifier.fillMaxWidth())


                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }






                    }
                    Options.JOIN_CLASS -> {
                        IconButton(
                            modifier = position,
                            onClick = { isSelectedOption = Options.NO_SELECTED }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }

                        Text(text = "Únete",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
//                        fontFamily = InterTight,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(horizontal = 5.dp,)
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                            // Input field

                            ItemInputField(
                                titulo = stringResource(id = R.string.join_class_text),

                                darkTheme = false,
                                valueField = input,
                                fieldRestriction = {
                                    val withoutWhiteSpace = it.removeSuffix(" ")
                                    if (withoutWhiteSpace != "" || it.isEmpty()) {
                                        withoutWhiteSpace
                                    } else {
                                        null
                                    }
                                },
                                valueOnChange = { input = it }
                            ){}



                        Spacer(modifier = Modifier.height(7.dp))



                        CustomButton(
                            onClick = {
                                if (state.isLoading) {
                                    // Handle loading
                                } else {
                                    if (input.isNotBlank()) {
                                        scope.launch {
                                            viewmodel.joinCourse(user.value!!.idApi, input)
                                        }
                                    }
                                }
                            },
                            text = "Unirse",
//                            isLoading = state.isLoading,
                            style = NavigationButtonStyle.SolidGradient,
                            color1 = Azul,
                            color2 = AzulGradient,
                            icon = Icons.Default.ArrowForwardIos,
                            disabled = state.isLoading,
                            modifier = Modifier.fillMaxWidth(0.85f))
                    }
                }

            }

        }

        LaunchedEffect(key1 = state, block = {
            when{
                state.isLoading -> {
                    dialogState = SetupCustomDialogState.Loading()
                }
                state.error != null -> {
                    dialogState = SetupCustomDialogState.Error(state.error.uiMessage)
                }

                else -> {
                    if (state.info != null){
                        dialogState = SetupCustomDialogState.Success(message = "Te has unido exitosamente al curso")
                        delay(1000)
                        dismissDialog()
                    }
                }
            }
        })

        SetupCustomDialog(
            setupCustomDialogState = dialogState,
            showDialog = dialogState != SetupCustomDialogState.Default()
        ) {
            dialogState = SetupCustomDialogState.Default()
        }
    }


}

enum class Options {
    NO_SELECTED,
    JOIN_CLASS
}