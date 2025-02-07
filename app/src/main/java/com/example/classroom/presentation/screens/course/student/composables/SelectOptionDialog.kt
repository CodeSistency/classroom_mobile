package com.example.classroom.presentation.screens.course.student.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.composables.ItemInputField
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.AzulGradient

@Composable
fun SelectedOptionDialogStudent(
    dismissDialog: () -> Unit,
    navController: NavController,
    idCourse: String,
) {
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


                        CustomButton(
                            onClick = {
                                navController.navigate(
                                    Destination.ADD_POST_SCREEN.screenRoute + "?idCourse=${idCourse}"
                                )
                            },
                            text = "Crear publicación",
                            style = NavigationButtonStyle.OutlineWithIconGradient,
                            color1 = Azul,
                            color2 = AzulGradient,
                            icon = Icons.Default.ArrowForwardIos,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(5.dp))


                    }

                }

            }

        }
    }


}

enum class Options {
    NO_SELECTED,

}