package com.example.classroom.presentation.screens.course.posts

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom.R
import com.example.classroom.common.CustomDialog
import com.example.classroom.domain.model.entity.LocalPost
import com.example.classroom.presentation.theme.PaddingCustom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun CardPostItem(post: LocalPost, viewModel: PostsViewModel, scope: CoroutineScope) {
    val shape = RoundedCornerShape(PaddingCustom.MEDIUM.size)
    var isDeleteOpen by remember { mutableStateOf(false) }
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
                        text = post.title,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = post.content,
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Created at: ${post.createdAt}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light,
                        )
                    )
                }
                IconButton(onClick = {
                    isDeleteOpen = true
                }) {
                    Icon(
                        painterResource(id = R.drawable.ic_cancel),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .height(80.dp)
                .width(5.dp)
                .background(Color(0xFF4CAF50), RoundedCornerShape(PaddingCustom.MEDIUM.size))
                .align(Alignment.CenterStart),
        )
    }

    if (isDeleteOpen) {
        CustomDialog(
            message = "Are you sure you want to delete this post?",
            messageBtn = "Delete",
            loading = false,
            action = { scope.launch{
                viewModel.deletePostCourseRemote(post.idApi)
            }  },
            dismissDialog = { isDeleteOpen = false },
            icon = painterResource(id = R.drawable.ic_person_remove)
        )
    }
}