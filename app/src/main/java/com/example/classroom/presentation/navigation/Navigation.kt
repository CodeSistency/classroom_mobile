package com.example.classroom.presentation.navigation


import android.os.Build
import android.os.Build.VERSION_CODES.Q
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.classroom.App
import com.example.classroom.common.validator.ActivityDataValidator
import com.example.classroom.common.validator.CourseDataValidator
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.data.repository.ActivitiesRepositoryImpl
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.use_case.courses.InsertCourseUseCase
import com.example.classroom.domain.use_case.courses.UpdateCourseUseCase
import com.example.classroom.domain.use_case.signIn.SignInUseCase
import com.example.classroom.domain.use_case.signUp.SignUpUseCase
import com.example.classroom.presentation.SplashScreen
import com.example.classroom.presentation.screens.Quizz.addQuizz.CreateQuizzScreen
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.activity.addActivity.AddActivityScreen
import com.example.classroom.presentation.screens.activity.addActivity.AddActivityScreenNew
import com.example.classroom.presentation.screens.activity.addActivity.AddActivityViewModel
import com.example.classroom.presentation.screens.activity.studentEvaluations.StudentEvaluationsViewModel
import com.example.classroom.presentation.screens.activity.studentEvaluations.StudentsEvaluationsScreen
import com.example.classroom.presentation.screens.activity.userActivities.UserActivitiesScreen
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.screens.auth.signIn.SignInScreen
import com.example.classroom.presentation.screens.auth.signIn.SignInScreenNew
import com.example.classroom.presentation.screens.auth.signIn.SignInViewModel
import com.example.classroom.presentation.screens.auth.signUp.SignUpScreen
import com.example.classroom.presentation.screens.auth.signUp.SignUpScreenNew
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseScreen
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseScreenNew
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseViewModel
import com.example.classroom.presentation.screens.course.CourseScreen
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.course.posts.addPost.composable.AddPostForm
import com.example.classroom.presentation.screens.home.HomeScreen
import com.example.classroom.presentation.screens.home.HomeViewmodel
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import com.example.classroom.presentation.screens.submission.professor.SubmissionProfessorScreen
import com.example.classroom.presentation.screens.submission.student.SubmissionStudentScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    isUserLogged: Boolean,
    darkTheme: Boolean,
    changeTheme: () -> Unit,
) {


    val navController = rememberNavController()
    val focusManager = LocalFocusManager.current

    NavHost(
        navController = navController,
//        startDestination = Destination.LOGIN.screenRoute,
        startDestination =
        if (isUserLogged) {
                Destination.HOME.screenRoute
        }else {
            Destination.LOGIN.screenRoute
        },
        builder = {
            /** Configuracion de vista de splash*/
            composable(
                route = Destination.SPLASH.screenRoute
            ){
                Box(modifier = Modifier.fillMaxSize()){
                    SplashScreen(navController)
                }
            }
            /** Configuracion de vista de inicio de sesion*/
            composable(
                route = Destination.LOGIN.screenRoute
            ){
                SignInScreenNew(
                    viewModel = App.appModule.signInViewModel,
                    focusManager = focusManager,
                    navController = navController,
                    darkTheme = false)
//                SignInScreen(
//                    viewModel = AuthViewModel(
//                        signUpValidator = App.appModule.validatorBundle.signUpValidator,
//                        signInValidator = App.appModule.validatorBundle.signInValidator,
//                        userDataValidator = UserDataValidator(),
//                        signUpUseCase = SignUpUseCase(App.appModule.repositoryBundle),
//                        signInUseCase = SignInUseCase(App.appModule.repositoryBundle),
//                        loginRepositoryImp = LoginRepositoryImpl(App.appModule.apiService, App.appModule.db.appDao),
//
//                    ),
//                    focusManager = focusManager,
//                    navController = navController,
//                    darkTheme = false)
            }
            /** Configuracion de vista de Home*/
            composable(
                route = Destination.HOME.screenRoute
            ){
                Box(modifier = Modifier.fillMaxSize()){
                    HomeScreen(
                        navController = navController,
                        viewmodel = App.appModule.homeViewModel,
                        addCourseViewModel = App.appModule.addCourseViewModel
                    )
                }
            }
            /** Configuracion de vista de Registro*/
            composable(
                route = Destination.REGISTRO.screenRoute
            ){
                SignUpScreenNew(
                    viewModel = App.appModule.authViewModel,
                    focusManager = focusManager,
                    navController = navController,
                    darkTheme = false)
//                SignUpScreen(
//                    viewModel = AuthViewModel(
//                        signUpValidator = App.appModule.validatorBundle.signUpValidator,
//                        signInValidator = App.appModule.validatorBundle.signInValidator,
//                        userDataValidator = UserDataValidator(),
//                        signUpUseCase = SignUpUseCase(App.appModule.repositoryBundle),
//                        signInUseCase = SignInUseCase(App.appModule.repositoryBundle),
//                        loginRepositoryImp = LoginRepositoryImpl(App.appModule.apiService, App.appModule.db.appDao),
//                    ),
//                    focusManager = focusManager,
//                    navController = navController,
//                    darkTheme = false)
            }

            composable(
                route = "${Destination.REGISTRO_ACTIVITY.screenRoute}?idCourse={idCourse}&email={email}&id={id}",
                arguments = listOf(
                    navArgument("idCourse") { nullable = false },
                    navArgument("email") { nullable = false },
                    navArgument("id") { nullable = true })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val idCourse = backStackEntry.arguments?.getString("idCourse")
                val email = backStackEntry.arguments?.getString("email")

                if (idCourse != null && email != null) {
                    AddActivityScreenNew(
                        idCourse,
                        email = email,
                        id = id,
                        viewModel = App.appModule.addActivityViewmodel, // Assuming you have a RegisterViewModel

                        navController = navController,
                        focusManager = focusManager
                    )
                }

//                if (idCourse != null && email != null) {
//                    AddActivityScreen(
//                        idCourse,
//                        email = email,
//                        id = id,
//                        viewModel = ActivityViewmodel(
//                            activityDataValidator = ActivityDataValidator(),
//                            insertActivityUseCase = App.appModule.insertActivityUseCase,
//                            repositoryBundle = App.appModule.repositoryBundle,
//                            updateActivityUseCase = App.appModule.updateActivityUseCase,
//                            getActivitiesUseCase = App.appModule.getActivitiesUseCase,
//                            activitiesValidator = App.appModule.validatorBundle.activitiesValidator,
//                            getActivitiesByUserUseCase = App.appModule.getActivitiesByUserUseCase
//                        ), // Assuming you have a RegisterViewModel
//                        navController = navController,
//                        focusManager = focusManager
//                    )
//                }
            }

            composable(
                route = "${Destination.REGISTRO_COURSE.screenRoute}?id={id}",
                arguments = listOf(

                    navArgument("id") { nullable = true }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")

                AddCourseScreenNew(
                    id = id,
                    viewModel = App.appModule.addCourseViewModel, // Assuming you have a RegisterViewModel
                    navController = navController,
                    focusManager = focusManager,
                    coursesViewModel = App.appModule.courseViewmodel, // Assum
                )

//                    AddCourseScreen(
//                        id = id,
//                        viewModel = CourseViewmodel(
//                            repositoryBundle = App.appModule.repositoryBundle,
//                            courseDataValidator = CourseDataValidator(),
//                            getActivitiesUseCase = App.appModule.getActivitiesUseCase,
//                            insertCourseUseCase = App.appModule.insertCourseUseCase,
//                            updateCourseUseCase = App.appModule.updateCourseUseCase,
//                            coursesValidator = App.appModule.validatorBundle.coursesValidator,
//                            getCoursesByIdUseCase = App.appModule.getCoursesByIdUseCase,
//                            joinUserToCourseUseCase = App.appModule.joinUserToCourseUseCase,
//                            getUsersByCourseUseCase = App.appModule.getUsersByCourseUseCase,
//                            ), // Assuming you have a RegisterViewModel
//                        navController = navController,
//                        focusManager = focusManager
//                    )

            }
            composable(
                route = "${Destination.COURSES.screenRoute}?id={id}&email={email}&isOwner={isOwner}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType; nullable = false },
                    navArgument("email") { nullable = false },
                    navArgument("isOwner") { type = NavType.BoolType; nullable = false }
                    )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val email = backStackEntry.arguments?.getString("email")
                val isOwner = backStackEntry.arguments?.getBoolean("isOwner")
                if (id != null && isOwner != null && email != null) {
                    CourseScreen(
                        id = id,
                        email = email,
                        viewModel = App.appModule.courseViewmodel,
                        activityViewmodel = App.appModule.activityViewModel,
                        addActivityViewModel = App.appModule.addActivityViewmodel,
                        navController = navController,
                        focusManager = focusManager,
                        isOwner = isOwner,
                        addCourseViewModel = App.appModule.addCourseViewModel
                    )
                }
            }
            composable(
                route = "${Destination.USER_ACTIVITIES.screenRoute}?id={id}&name={name}&course={course}",
                arguments = listOf(
                    navArgument("id") { type = NavType.StringType; nullable = false },
                    navArgument("name") { nullable = false },
                    navArgument("course") {  nullable = false }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val name = backStackEntry.arguments?.getString("name")
                val course = backStackEntry.arguments?.getString("course")
                if (id != null && name != null && course != null) {
                    UserActivitiesScreen(
                        id = id,
                        name = name,
                        course = course,
                        courseViewmodel = App.appModule.courseViewmodel,
                        viewModel = App.appModule.activityViewModel,
                        navController = navController,
                    )
                }
            }

            composable(
                route = "${Destination.STUDENT_EVALUATIONS.screenRoute}?idStudent={idStudent}&idCourse={idCourse}",
                arguments = listOf(
                    navArgument("idStudent") { type = NavType.StringType; nullable = false },
                    navArgument("idCourse") { type = NavType.StringType; nullable = false },
                )
            ) { backStackEntry ->
                val idStudent = backStackEntry.arguments?.getString("idStudent")
                val idCourse = backStackEntry.arguments?.getString("idCourse")
                if (idStudent != null && idCourse != null ) {

                    StudentsEvaluationsScreen(
                        viewModel = App.appModule.studentEvaluationsViewModel,
                        courseId = idCourse,
                        studentId = idStudent,
                        navController = navController,
                    )
                }
            }



            composable(
                route = "${Destination.STUDENT_UPLOAD_EVALUATION.screenRoute}?idStudent={idStudent}&idActivity={idActivity}&idCourse={idCourse}",
                arguments = listOf(
                    navArgument("idStudent") { type = NavType.StringType; nullable = false },
                    navArgument("idActivity") { type = NavType.StringType; nullable = false },
                )
            ) { backStackEntry ->
                val idStudent = backStackEntry.arguments?.getString("idStudent")
                val idActivity = backStackEntry.arguments?.getString("idActivity")
                val idCourse = backStackEntry.arguments?.getString("idCourse")
                if (idStudent != null && idActivity != null && idCourse != null  ) {

                    SubmissionStudentScreen(
                        viewModel = App.appModule.submissionViewModel,
                        activityId = idActivity,
                        studentId = idStudent,
                        courseId = idCourse,
//                        navController = navController,
                    )
                }
            }

            composable(
                route = "${Destination.PROFESSOR_REVIEW_EVALUATION.screenRoute}?idStudent={idStudent}&idActivity={idActivity}&idCourse={idCourse}",
                arguments = listOf(
                    navArgument("idStudent") { type = NavType.StringType; nullable = false },
                    navArgument("idActivity") { type = NavType.StringType; nullable = false },
                )
            ) { backStackEntry ->
                val idStudent = backStackEntry.arguments?.getString("idStudent")
                val idActivity = backStackEntry.arguments?.getString("idActivity")
                val idCourse = backStackEntry.arguments?.getString("idCourse")

                if (idStudent != null && idActivity != null && idCourse != null ) {

                    SubmissionProfessorScreen(
                            viewModel = App.appModule.submissionViewModel,
                        activityId = idActivity,
                        studentId = idStudent,
                        courseId = idCourse
//                        navController = navController,
                    )
                }
            }

            composable(
                route = "${Destination.ADD_QUIZZ.screenRoute}?idCourse={idCourse}",
                arguments = listOf(
                    navArgument("idCourse") { type = NavType.StringType; nullable = false },
                )
            ) { backStackEntry ->
                val idCourse = backStackEntry.arguments?.getString("idCourse")
                if (idCourse != null ) {

                    CreateQuizzScreen(
                        viewModel = App.appModule.quizzViewModel,
                        courseId = idCourse,
                        navController = navController,
                    )
                }
            }

            composable(
                route = "${Destination.ADD_POST_SCREEN.screenRoute}?idCourse={idCourse}",
                arguments = listOf(
                    navArgument("idCourse") { type = NavType.StringType; nullable = false },
                )
            ) { backStackEntry ->
                val idCourse = backStackEntry.arguments?.getString("idCourse")
                if (idCourse != null ) {

                    AddPostForm(
                        viewModel = App.appModule.addPostViewModel,
                        focusManager = focusManager,
                        courseId = idCourse,
                        onSubmit = {},
                    )
                }
            }

        }
    )
}

@RequiresApi(Q)
fun Modifier.advancedShadow(
    color: Color = Color.Black,
    alpha: Float = 1f,
    cornersRadius: Dp = 0.dp,
    shadowBlurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp
) = drawBehind {

    val shadowColor = color.copy(alpha = alpha).toArgb()
    val transparentColor = color.copy(alpha = 0f).toArgb()

    drawIntoCanvas {
        val paint = Paint()
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.color = transparentColor
        frameworkPaint.setShadowLayer(
            shadowBlurRadius.toPx(),
            offsetX.toPx(),
            offsetY.toPx(),
            shadowColor
        )
        it.drawRoundRect(
            0f,
            0f,
            this.size.width,
            this.size.height,
            cornersRadius.toPx(),
            cornersRadius.toPx(),
            paint
        )
    }
}