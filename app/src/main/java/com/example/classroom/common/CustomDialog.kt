package com.example.classroom.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun CustomDialog(
    message: String,
    messageBtn: String,
    loading: Boolean,
    action: () -> Unit,
    dismissDialog: () -> Unit, // Lambda to dismiss the dialog
    icon: Painter
) {
    Dialog(
        onDismissRequest = dismissDialog,
    ) {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

                Icon(icon, contentDescription = "custom dialog", Modifier.size(100.dp))
                Text(text = message,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 18.sp,
//                        fontFamily = InterTight,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 5.dp)
                )


            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
            } else {
                Button(

                    onClick = {
                        action()
                        dismissDialog()
                    }, modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(0.70f),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Azul)
                ) {
                    Text(text = messageBtn,
                        color = Color.White,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
//                            fontFamily = InterTight
                        )
                    )
                }
            }

        }
    }


}