package com.example.classroom.presentation.screens.course.student.composables

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SendAndArchive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.PaddingCustom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CardActivity(
    activity: LocalActivities,
    userId: String,
    courseId: String,
    navController: NavController,
    action: () -> Unit
) {
    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
    var isSendActivityOpen by remember { mutableStateOf(false) }

    // Date comparison
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currentDate = Date()
    val endDate = dateFormatter.parse(activity.endDate) ?: currentDate
    val isDatePast = endDate.before(currentDate)

    Box(modifier = Modifier) {
        Box(
            modifier = Modifier
                .shadow(8.dp, shape)
                .background(Color.White, shape)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = activity.title,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    if (isDatePast) {
                        Text(
                            text = "La fecha ya pasó",
                            style = TextStyle(
                                color = Color.DarkGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    } else {
                        Column {
                            Text(
                                text = "Fecha de inicio: ${activity.startDate}",
                                style = TextStyle(
                                    color = Color.DarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Fecha de finalización: ${activity.endDate}",
                                style = TextStyle(
                                    color = Color.DarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            )
                        }
                    }
                }

                // Disable interaction if the date has passed
                if (!isDatePast) {
                    IconButton(onClick = {
                        if (activity.isQuizz) {
                            navController.navigate(
                                "${Destination.ANSWER_QUIZZ.screenRoute}?quizzId=${activity.quizzId}"
                            )
                        } else {
                            navController.navigate(
                                "${Destination.STUDENT_UPLOAD_EVALUATION.screenRoute}?idStudent=$userId&idActivity=${activity.idApi}&idCourse=$courseId"
                            )
                        }
                    }) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                } else {
                    Icon(
                        Icons.Default.Block,
                        contentDescription = "Disabled",
                        tint = Color.LightGray,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(80.dp)
                .width(5.dp)
                .background(Azul2, RoundedCornerShape(PaddingCustom.MEDIUM.size))
                .align(Alignment.CenterStart)
        )
    }

    // Commented-out dialog if needed in the future
    // if (isSendActivityOpen){
    //     SendActivityDialog(
    //         action = { action() },
    //         onDismissRequest = { isSendActivityOpen = false },
    //     )
    // }
}

//@Composable
//fun CardActivity(
//    activity: LocalActivities,
//    userId: String,
//    courseId: String,
//    navController: NavController,
////    msgDelete: String,
////    msgDeleteBtn: String,
//    action: () -> Unit
//){
//    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
//    var isSendActivityOpen by remember { mutableStateOf(false) }
//    Box(modifier = Modifier){
//        Box(
//            modifier = Modifier
//                .shadow(8.dp, shape)
//                .background(Color.White, shape)
//                .padding(16.dp)
//
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                Arrangement.SpaceBetween
//            ){
//                Column {
//                    Text(
//                        text = activity.title,
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    when(activity.status){
//                        Status.LATE -> {
//                            Text(
//                                text = "La fecha ya pasó",
//                                style = TextStyle(
//                                    color = Color.DarkGray,
//                                    fontSize = 10.sp,
//                                    fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                                )
//                            )
//                        }
//                        Status.OPEN -> {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Text(
//                                    text = "Fecha de finalización:",
//                                    style = TextStyle(
//                                        color = Color.DarkGray,
//                                        fontSize = 10.sp,
//                                        fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                                    )
//                                )
//                                Spacer(modifier = Modifier.width(3.dp))
//                                Text(
//                                    text = activity.endDate,
//                                    style = TextStyle(
//                                        color = Color.DarkGray,
//                                        fontSize = 10.sp,
//                                        fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                                    )
//                                )
//                            }
//                        }
//                        Status.FINISHED -> {
//                            Text(
//                                text = "La actividad está cerrada",
//                                style = TextStyle(
//                                    color = Color.DarkGray,
//                                    fontSize = 10.sp,
//                                    fontWeight = FontWeight.Bold,
////                            fontFamily = InterTight
//                                )
//                            )
//                        }
//
//                        else -> {}
//                    }
//                }
//                when(activity.status){
//                    Status.LATE -> TODO()
//                    Status.OPEN -> {
//                        IconButton(onClick = {
//                            if (activity.isQuizz){
//                                navController.navigate(
//                                    "${Destination.ANSWER_QUIZZ.screenRoute}?quizzId=${activity.quizzId}"
//                                )
//                            }else{
//                                navController.navigate(
//                                    "${Destination.STUDENT_UPLOAD_EVALUATION.screenRoute}?idStudent=${userId}&idActivity=${activity.idApi}&idCourse=${courseId}"
//                                )
//                            }
////                            isSendActivityOpen = true
//                        }) {
//                            Icon(
//                                Icons.Default.Send,
//                                contentDescription = null,
//                                tint = Color.Gray,
//                                modifier = Modifier.size(35.dp)
//                            )
//                        }
//
//                    }
//                    Status.FINISHED -> TODO()
//                    else -> {}
//                }
//            }
//        }
//        Box(modifier = Modifier
//            .height(80.dp)
//            .width(5.dp)
//            .background(Azul2, RoundedCornerShape(PaddingCustom.MEDIUM.size))
//            .align(Alignment.CenterStart),)
//    }
//
////    if (isSendActivityOpen){
////        SendActivityDialog(
////            action = { action() },
////            onDismissRequest = { isSendActivityOpen = false },
////        )
//
////    }
//
//}

@Composable
fun SendActivityDialog(onDismissRequest: () -> Unit, action: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() },) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Envio de la actividad",
                    textAlign = TextAlign.Center,
                )
            }

        }
    }
}
