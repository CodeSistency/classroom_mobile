package com.example.classroom.common.composables.customDialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.presentation.theme.PaddingCustom
import com.example.classroom.common.composables.customDialogs.dialogs.ErrorDialog
import com.example.classroom.common.composables.customDialogs.dialogs.LoadingDialog
import com.example.classroom.common.composables.customDialogs.dialogs.SuccessDialog
import com.example.classroom.common.composables.customDialogs.dialogs.WarningDialog


sealed class SetupCustomDialogState (val messageDialog: String? = null){
    class Default(): SetupCustomDialogState()
    class Loading(): SetupCustomDialogState()
    class Warning(message: String?): SetupCustomDialogState(messageDialog = message)
    class Success(message: String?): SetupCustomDialogState(messageDialog = message)
    class Error(message: String?): SetupCustomDialogState(messageDialog = message)
}
@Composable
fun SetupCustomDialog(
    setupCustomDialogState: SetupCustomDialogState,
    showDialog: Boolean,
    onDismissRequest: () -> Unit = {},
    customClick: ()-> Unit = {},
    onClick: ()-> Unit
){
    when(setupCustomDialogState){
    is SetupCustomDialogState.Default -> {

    }
        is SetupCustomDialogState.Error -> {
        ErrorDialog(message = setupCustomDialogState.messageDialog ?: "", onDismissRequest = onDismissRequest) {
            onClick()
        }
        }
    is SetupCustomDialogState.Success -> {
        SuccessDialog(message = setupCustomDialogState.messageDialog ?: "", onDismissRequest = onDismissRequest) {
        onClick()            }
    }
        is SetupCustomDialogState.Warning -> {
        WarningDialog(
            message = setupCustomDialogState.messageDialog ?: "",
            onDismissRequest = onDismissRequest,
            secondaryClick = customClick){
            onClick()
            }
        }
    is SetupCustomDialogState.Loading -> {
        LoadingDialog()
    }    }
}

@Composable
fun CardDialogParent(
    showButton: Boolean = false,
    title: String,
    color: Color,
    isLoading: Boolean = false,
    message: String? = null,
    nomenclature: String? = null,
    isForTwoButtons: Boolean = false,
    titleButtonPrimary: String = "Aceptar",
    titleButtonSecondary: String = "",
    onClick: () -> Unit = {},
    onClickCancel: (() -> Unit)? = null
){
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageInfo = packageManager.getPackageInfo(context.packageName, 0)

    Card(

        shape = RoundedCornerShape(32.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = TextStyle(
                    fontSize = 24.sp,
//                    fontFamily = InterTight,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = color
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (title == "Cargando"){
                CircularProgressIndicator(Modifier.size(30.dp), color = color)
            }

            /*val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))
            val progress by if (isLoading) animateLottieCompositionAsState(composition = composition, iterations = LottieConstants.IterateForever, speed = 1.5f) else animateLottieCompositionAsState(composition = composition)

            LottieAnimation(
                modifier = Modifier.size(128.dp),
                composition = composition, progress = { progress },
            )*/

            if (message != null){
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = PaddingCustom.MEDIUM.size),
                    text = message,
                    style = TextStyle(
                        fontSize = 16.sp,
//                        fontFamily = InterTight,
                        textAlign = TextAlign.Center,
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (showButton){

                Spacer(modifier = Modifier.height(12.dp))


                if (isForTwoButtons){
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(end = 6.dp, start = 12.dp),
                            onClick = {
                                if (onClickCancel != null) {
                                    onClickCancel()
                                }
                            },
                            shape = RoundedCornerShape(PaddingCustom.HORIZONTAL_STANDARD.size),
                            border = BorderStroke(1.dp, color),
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = PaddingCustom.SMALL.size),
                                text = titleButtonSecondary,
                                style = TextStyle(
                                    fontSize = 12.sp,
//                                    fontFamily = InterTight,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = color
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp, end = 12.dp),
                            onClick = {
                                onClick()
                            },
                            shape = RoundedCornerShape(PaddingCustom.HORIZONTAL_STANDARD.size),
                            colors = ButtonDefaults.buttonColors(backgroundColor = color),
                            elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = PaddingCustom.SMALL.size),
                                text = titleButtonPrimary,
                                style = TextStyle(
                                    fontSize = 12.sp,
//                                    fontFamily = InterTight,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }else{
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, end = 12.dp),
                        onClick = {
                            onClick()
                        },
                        shape = RoundedCornerShape(PaddingCustom.HORIZONTAL_STANDARD.size),
                        colors = ButtonDefaults.buttonColors(backgroundColor = color),
                        elevation = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = PaddingCustom.SMALL.size),
                            text = titleButtonPrimary,
                            style = TextStyle(
                                fontSize = 12.sp,
//                                fontFamily = InterTight,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }else{
                Spacer(modifier = Modifier.height(PaddingCustom.HORIZONTAL_STANDARD.size))
            }
        }
    }
}