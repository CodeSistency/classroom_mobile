package com.example.classroom.presentation.screens.course.student.composables

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.common.composables.cardWrapper.CardWrapper
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.PaddingCustom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun CardActivitySubmitted(
    evaluation: LocalActivitySubmission,
    viewModel: CourseViewmodel,
    navController: NavController
) {
    val activities by viewModel.listActivitiesFlow.collectAsState()

    val activity = activities.firstOrNull { it.idApi == evaluation.activityId }

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC") // Parse in UTC
    }

    val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US) // Desired output format

    val formattedDate = try {
        val date = inputFormat.parse(evaluation.submissionDate)
        outputFormat.format(date ?: Date()) // Format the date properly
    } catch (e: Exception) {
        "2000/01/01" // Default fallback in case of error
    }

    if (activity != null) {
        Box(modifier = Modifier.fillMaxWidth()){
            CardWrapper {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Actividad: ${activity.title}",
                            style = TextStyle(
                                color = Color.DarkGray,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Fecha de evaluación: $formattedDate",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )

                        Text(
                            text = if (evaluation.grade > 0) "Calificación: ${evaluation.grade}" else "Sin calificación",
                            style = TextStyle(
                                color = if (evaluation.grade > 0) Color.Black else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        )
                    }
                }


            }

        }
    } else {
        Log.e("CardActivitySubmitted", "Activity with ID ${evaluation.activityId} not found.")
    }
}


//@Composable
//fun CardActivitySubmitted(
//    evaluation: LocalActivitySubmission,
//    viewModel: CourseViewmodel,
//    navController: NavController
//) {
//    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
//
//    val activities by viewModel.listActivitiesFlow.collectAsState()
//
////    val activity = activities.first { it.idApi == evaluation.activityId }
//    val activity = activities.firstOrNull { it.idApi == evaluation.idApi }
//
//    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
//        timeZone = TimeZone.getTimeZone("UTC") // Parse in UTC
//    }
//
//    val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US) // Desired output format
//
//    val formattedDate = try {
//        val date = inputFormat.parse(evaluation.submissionDate)
//        outputFormat.format(date ?: Date()) // Format the date properly
//    } catch (e: Exception) {
//        "2000/01/01" // Default fallback in case of error
//    }
//
//    if (activity != null) {
//        Box(modifier = Modifier) {
//            Box(
//                modifier = Modifier
//                    .shadow(8.dp, shape)
//                    .background(Color.White, shape)
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column {
//                        Text(
//                            text = "Actividad: ${activity.title}",
//                            style = TextStyle(
//                                color = Color.DarkGray,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                            )
//                        )
//                        Spacer(modifier = Modifier.height(5.dp))
//                        Text(
//                            text = "Fecha de evaluación: $formattedDate",
//                            style = TextStyle(
//                                color = Color.Gray,
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Bold,
//                            )
//                        )
//
//                        if (evaluation.grade > 0) {
//                            Text(
//                                text = "Calificación: ${evaluation.grade}",
//                                style = TextStyle(
//                                    color = Color.Black,
//                                    fontSize = 10.sp,
//                                    fontWeight = FontWeight.Bold,
//                                )
//                            )
//                        } else {
//                            Text(
//                                text = "Sin calificación",
//                                style = TextStyle(
//                                    color = Color.Gray,
//                                    fontSize = 10.sp,
//                                    fontWeight = FontWeight.Bold,
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .height(90.dp)
//                    .width(5.dp)
//                    .background(Azul2, shape)
//                    .align(Alignment.CenterStart)
//            )
//        }
//    } else {
//        Log.e("CardActivitySubmitted", "Activity with ID ${evaluation.activityId} not found.")
//    }
//
//}
