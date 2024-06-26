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
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.screens.home.HomeViewmodel
import com.example.classroom.presentation.theme.Azul
import kotlinx.coroutines.CoroutineScope
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
        Box(modifier = Modifier){
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
                        Text(text = "Selecciona una opcion",
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
//                        fontFamily = InterTight,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(horizontal = 5.dp,)
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                isSelectedOption = Options.JOIN_CLASS
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Azul
                            ),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                Arrangement.SpaceBetween,
                                Alignment.CenterVertically
                            ) {
                                Text(text = "Unirse a una clase", modifier = Modifier.weight(1f))
                                Icon(Icons.Default.ArrowForwardIos, contentDescription = null)
                            }

                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(
                            onClick = {
                                navController.navigate(Destination.REGISTRO_COURSE.screenRoute)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Azul
                            ),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                Arrangement.SpaceBetween,
                                Alignment.CenterVertically
                            ) {
                                Text(text = "Crear una clase", modifier = Modifier.weight(1f))
                                Icon(Icons.Default.ArrowForwardIos, contentDescription = null)
                            }

                        }
                    }
                    Options.JOIN_CLASS -> {
                        IconButton(
                            modifier = position,
                            onClick = { isSelectedOption = Options.NO_SELECTED }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                        if (state.isLoading){
                            Box(modifier = Modifier.fillMaxSize()){
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                            }
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically
                        ){
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
                                valueOnChange = {
                                    input = it
                                }
                            ) {
                            }
                            IconButton(onClick = {
                                if (state.isLoading){

                                }else{
                                    if (input.isNotBlank()){
                                        scope.launch {
                                            viewmodel.joinCourse(user.value!!.idApi, input)
                                        }
                                    }
                                }
                            }) {
                                Icon(Icons.Default.ArrowForwardIos,
                                    contentDescription = null,
                                    tint = Color.Black)
                            }

                        }
                    }
                }

            }

        }
    }


}

enum class Options {
    NO_SELECTED,
    JOIN_CLASS
}