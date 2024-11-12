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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.classroom.domain.model.entity.LocalActivitySubmission
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.theme.Azul2
import com.example.classroom.presentation.theme.PaddingCustom


@Composable
fun CardActivitySubmitted(
    evaluation: LocalActivitySubmission,
    idStudent: String,
    idCourse: String,
    navController: NavController
) {
    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)

    Box(modifier = Modifier){
        Box(
            modifier = Modifier
                .shadow(8.dp, shape)
                .background(Color.White, shape)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Actividad: ${evaluation.activityId}",
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            // fontFamily = InterTight (uncomment if using custom font)
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Fecha de evaluación: ${evaluation.submissionDate}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            // fontFamily = InterTight (uncomment if using custom font)
                        )
                    )

                    // Display grade if available
                    if (evaluation.grade > 0) {
                        Text(
                            text = "Calificación: ${evaluation.grade}",
                            style = TextStyle(
                                color = MaterialTheme.colors.primary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                // fontFamily = InterTight (uncomment if using custom font)
                            )
                        )
                    } else {
                        Text(
                            text = "Sin calificación",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                // fontFamily = InterTight (uncomment if using custom font)
                            )
                        )
                    }
                }

                // Optional Icon Buttons for editing or deleting (similar to CardActivity)
//                Row {
//                    IconButton(onClick = {
//                        navController.navigate("${Destination.PROFESSOR_REVIEW_EVALUATION.screenRoute}?idStudent=${idStudent}&idActivity=${evaluation.idApi}&idCourse=${idCourse}")
//                    }) {
//                        Icon(
//                            Icons.Default.Edit,
//                            contentDescription = "Edit Evaluation",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(25.dp)
//                        )
//                    }
//                    IconButton(onClick = {
//                        // Handle delete or additional actions
//                    }) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_cancel),
//                            contentDescription = "Delete Evaluation",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(25.dp)
//                        )
//                    }
//                }
            }
        }
// Optional left bar for styling (if needed)

        Box(
            modifier = Modifier
                .height(90.dp)
                .width(5.dp)
                .background(Azul2, shape)
                .align(Alignment.CenterStart)
        )
    }


}
