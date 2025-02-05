package com.example.classroom.presentation.screens.activity.studentEvaluations.composable

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.R
import com.example.classroom.common.composables.cardWrapper.CardWrapper
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.activity.studentEvaluations.StudentEvaluationsViewModel
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.PaddingCustom
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EvaluationItem(
    evaluation: LocalActivitySubmission,
    idStudent: String,
    idCourse: String,
    navController: NavController,
    viewModel: StudentEvaluationsViewModel,
) {
    val activities by viewModel.listActivitiesFlow.collectAsState()

    val activity = activities.first { it.idApi == evaluation.activityId }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = dateFormatter.parse(activity.endDate)

    CardWrapper(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Actividad: ${activity.title}",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Fecha de evaluación: $date",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                // Display grade if available
                if (evaluation.grade > 0) {
                    Text(
                        text = "Calificación: ${evaluation.grade}",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Text(
                        text = "Sin calificación",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            if (evaluation.grade < 1) {
                IconButton(onClick = {
                    navController.navigate("${Destination.PROFESSOR_REVIEW_EVALUATION.screenRoute}?idStudent=${idStudent}&idActivity=${evaluation.activityId}&idCourse=${idCourse}")
                }) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null)
                }
            }
        }
    }

    // Optional left bar for styling (if needed)
//    Box(
//        modifier = Modifier
//            .height(90.dp)
//            .width(5.dp)
//            .background(Azul2, RoundedCornerShape(PaddingCustom.MEDIUM.size))
//            .align(Alignment.CenterStart)
//    )
}


//@Composable
//fun EvaluationItem(
//    evaluation: LocalActivitySubmission,
//    idStudent: String,
//    idCourse: String,
//    navController: NavController,
//    viewModel: StudentEvaluationsViewModel,
//) {
//    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
//
//    val activities by viewModel.listActivitiesFlow.collectAsState()
//
//    val activity = activities.first { it.idApi == evaluation.activityId }
//
//    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//    val date = dateFormatter.parse(activity.endDate)
//
//    Box(modifier = Modifier){
//        Box(
//            modifier = Modifier
//                .shadow(8.dp, shape)
//                .background(Color.White, shape)
//                .fillMaxWidth()
//                .clickable {
//                    if (evaluation.grade < 1) {
//                        navController.navigate("${Destination.PROFESSOR_REVIEW_EVALUATION.screenRoute}?idStudent=${idStudent}&idActivity=${evaluation.activityId}&idCourse=${idCourse}")
//                    }
//                }
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = "Actividad: ${activity.title}",
//                        style = TextStyle(
//                            color = Color.DarkGray,
//                            fontSize = 20.sp,
//                            fontWeight = FontWeight.Bold,
//                            // fontFamily = InterTight (uncomment if using custom font)
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(5.dp))
//                    Text(
//                        text = "Fecha de evaluación: $date",
//                        style = TextStyle(
//                            color = Color.Gray,
//                            fontSize = 10.sp,
//                            fontWeight = FontWeight.Bold,
//                            // fontFamily = InterTight (uncomment if using custom font)
//                        )
//                    )
//
//                    // Display grade if available
//                    if (evaluation.grade > 0) {
//                        Text(
//                            text = "Calificación: ${evaluation.grade}",
//                            style = TextStyle(
//                                color = Color.Black,
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Bold,
//                                // fontFamily = InterTight (uncomment if using custom font)
//                            )
//                        )
//                    } else {
//                        Text(
//                            text = "Sin calificación",
//                            style = TextStyle(
//                                color = Color.Gray,
//                                fontSize = 10.sp,
//                                fontWeight = FontWeight.Bold,
//                                // fontFamily = InterTight (uncomment if using custom font)
//                            )
//                        )
//                    }
//                }
//
//                if (evaluation.grade < 1){
//                    IconButton(onClick = {
//                        navController.navigate("${Destination.PROFESSOR_REVIEW_EVALUATION.screenRoute}?idStudent=${idStudent}&idActivity=${evaluation.activityId}&idCourse=${idCourse}")
//
//                    }) {
//                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
//                    }
//                }
//
//
//                // Optional Icon Buttons for editing or deleting (similar to CardActivity)
////                Row {
////                    IconButton(onClick = {
////                        navController.navigate("${Destination.PROFESSOR_REVIEW_EVALUATION.screenRoute}?idStudent=${idStudent}&idActivity=${evaluation.idApi}&idCourse=${idCourse}")
////                    }) {
////                        Icon(
////                            Icons.Default.Edit,
////                            contentDescription = "Edit Evaluation",
////                            tint = Color.Gray,
////                            modifier = Modifier.size(25.dp)
////                        )
////                    }
////                    IconButton(onClick = {
////                        // Handle delete or additional actions
////                    }) {
////                        Icon(
////                            painter = painterResource(id = R.drawable.ic_cancel),
////                            contentDescription = "Delete Evaluation",
////                            tint = Color.Gray,
////                            modifier = Modifier.size(25.dp)
////                        )
////                    }
////                }
//            }
//        }
//// Optional left bar for styling (if needed)
//
//        Box(
//            modifier = Modifier
//                .height(90.dp)
//                .width(5.dp)
//                .background(Azul2, shape)
//                .align(Alignment.CenterStart)
//        )
//    }
//
//
//}
