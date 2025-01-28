package com.example.classroom.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.classroom.presentation.theme.Azul
import com.example.classroom.presentation.theme.Azul2

@Composable
fun CustomDialog(
    message: String,
    messageBtn: String,
    colorIcon: Color = Color.Gray,
    loading: Boolean,
    action: () -> Unit,
    dismissDialog: () -> Unit, // Lambda to dismiss the dialog
    icon: Painter
) {
    Dialog(
        onDismissRequest = dismissDialog,
    ) {
        Column(
            modifier = Modifier
                .shadow(10.dp, shape = RoundedCornerShape(20.dp))
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(20.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = "Dialog Icon",
                modifier = Modifier.size(120.dp),
                tint = colorIcon
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (loading) {
                CircularProgressIndicator()
            } else {
                RoundedButton(
                    text = messageBtn,
                    onClick = {
                        action()
                        dismissDialog()
                    },
                    modifier = Modifier.fillMaxWidth(0.75f)
                )
            }
        }
    }
}

@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Azul2,
    contentColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor, contentColor = contentColor),
        modifier = modifier
            .height(48.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button.copy(fontSize = 16.sp)
        )
    }
}

//@Composable
//fun CustomDialog(
//    message: String,
//    messageBtn: String,
//    loading: Boolean,
//    action: () -> Unit,
//    dismissDialog: () -> Unit, // Lambda to dismiss the dialog
//    icon: Painter
//) {
//    Dialog(
//        onDismissRequest = dismissDialog,
//    ) {
//        val context = LocalContext.current
//        Column(
//            modifier = Modifier
//                .shadow(8.dp, shape = RoundedCornerShape(16.dp))
//                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
//                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//                Icon(icon, contentDescription = "custom dialog", Modifier.size(100.dp))
//                Text(text = message,
//                    style = TextStyle(
//                        fontWeight = FontWeight.Normal,
//                        fontSize = 18.sp,
////                        fontFamily = InterTight,
//                        textAlign = TextAlign.Center
//                    ),
//                    modifier = Modifier.padding(horizontal = 5.dp)
//                )
//
//
//            if (loading) {
//                CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
//            } else {
//                Button(
//
//                    onClick = {
//                        action()
//                        dismissDialog()
//                    }, modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(0.70f),
//                    colors = ButtonDefaults.buttonColors(backgroundColor = Azul)
//                ) {
//                    Text(text = messageBtn,
//                        color = Color.White,
//                        style = TextStyle(
//                            fontWeight = FontWeight.Normal,
//                            fontSize = 15.sp,
////                            fontFamily = InterTight
//                        )
//                    )
//                }
//            }
//
//        }
//    }
//
//
//}