package com.example.classroom.common.composables.customDialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    customClick: () -> Unit = {},
    onClick: () -> Unit
) {
    if (!showDialog) return

    when (setupCustomDialogState) {
        is SetupCustomDialogState.Default -> {}
        is SetupCustomDialogState.Error -> {
            StyledDialog(
                title = "Error!",
                icon = Icons.Default.Error,
                iconColor = Color.Red,
                message = setupCustomDialogState.messageDialog ?: "An unexpected error occurred.",
                buttonText = "CLOSE",
                buttonColor = Color.Red,
                onDismissRequest = onDismissRequest,
                onClick = onClick
            )
        }
        is SetupCustomDialogState.Success -> {
            StyledDialog(
                title = "Success!",
                icon = Icons.Default.CheckCircle,
                iconColor = Color.Green,
                message = setupCustomDialogState.messageDialog ?: "Operation completed successfully.",
                buttonText = "OK",
                buttonColor = Color.Green,
                onDismissRequest = onDismissRequest,
                onClick = onClick
            )
        }
        is SetupCustomDialogState.Warning -> {
            StyledDialog(
                title = "Warning!",
                icon = Icons.Default.Warning,
                iconColor = Color.Yellow,
                message = setupCustomDialogState.messageDialog ?: "Please be cautious.",
                buttonText = "UNDERSTOOD",
                buttonColor = Color.Yellow,
                onDismissRequest = onDismissRequest,
                secondaryButtonText = "CANCEL",
                secondaryClick = customClick,
                onClick = onClick
            )
        }
        is SetupCustomDialogState.Loading -> {
            LoadingDialog()
        }
    }
}
//@Composable
//fun SetupCustomDialog(
//    setupCustomDialogState: SetupCustomDialogState,
//    showDialog: Boolean,
//    onDismissRequest: () -> Unit = {},
//    customClick: ()-> Unit = {},
//    onClick: ()-> Unit
//){
//    when(setupCustomDialogState){
//    is SetupCustomDialogState.Default -> {
//
//    }
//        is SetupCustomDialogState.Error -> {
//        ErrorDialog(message = setupCustomDialogState.messageDialog ?: "", onDismissRequest = onDismissRequest) {
//            onClick()
//        }
//        }
//    is SetupCustomDialogState.Success -> {
//        SuccessDialog(message = setupCustomDialogState.messageDialog ?: "", onDismissRequest = onDismissRequest) {
//        onClick()            }
//    }
//        is SetupCustomDialogState.Warning -> {
//        WarningDialog(
//            message = setupCustomDialogState.messageDialog ?: "",
//            onDismissRequest = onDismissRequest,
//            secondaryClick = customClick){
//            onClick()
//            }
//        }
//    is SetupCustomDialogState.Loading -> {
//        LoadingDialog()
//    }    }
//}



@Composable
fun StyledDialog(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    message: String,
    buttonText: String,
    buttonColor: Color,
    onDismissRequest: () -> Unit = {},
    secondaryButtonText: String? = null,
    secondaryClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = iconColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (secondaryButtonText != null && secondaryClick != null) {
                        OutlinedButton(
                            onClick = secondaryClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = secondaryButtonText)
                        }
                    }
                    Button(
                        onClick = onClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
                    ) {
                        Text(text = buttonText, color = Color.White)
                    }
                }
            }
        }
    }
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