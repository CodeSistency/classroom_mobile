package com.example.classroom.di

import android.content.Context
import androidx.room.Room
import com.example.classroom.App
import com.example.classroom.common.validator.ActivityDataValidator
import com.example.classroom.common.validator.CourseDataValidator
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.data.remote.ApiService
import com.example.classroom.data.remote.ApiServiceImpl
import com.example.classroom.data.repository.ActivitiesRepositoryImpl
import com.example.classroom.data.repository.ChatRepositoryImpl
import com.example.classroom.data.repository.CloudRepositoryImpl
import com.example.classroom.data.repository.CoursesRepositoryImpl
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.data.repository.PostsRepositoryImpl
import com.example.classroom.data.repository.QuizzRepositoryImpl
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.data.repository.StudentsRepositoryImpl
import com.example.classroom.data.repository.SubmissionsRepositoryImpl
import com.example.classroom.domain.use_case.activities.DeleteActivityUseCase
import com.example.classroom.domain.use_case.activities.GetActivitiesByUserUseCase
import com.example.classroom.domain.use_case.activities.GetActivitiesUseCase
import com.example.classroom.domain.use_case.activities.InsertActivityUseCase
import com.example.classroom.domain.use_case.activities.UpdateActivityUseCase
import com.example.classroom.domain.use_case.cloud.UploadFileUseCase
import com.example.classroom.domain.use_case.courses.DeleteCourseUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesByIdUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesUseCase
import com.example.classroom.domain.use_case.courses.GetUsersByCourseUseCase
import com.example.classroom.domain.use_case.courses.InsertCourseUseCase
import com.example.classroom.domain.use_case.courses.JoinCourseUseCase
import com.example.classroom.domain.use_case.courses.JoinUserToCourseUseCase
import com.example.classroom.domain.use_case.courses.UpdateCourseUseCase
import com.example.classroom.domain.use_case.evaluations.getActivitiesSubmittedByStudent.GetActivitiesSubmitedByStudent
import com.example.classroom.domain.use_case.evaluations.professorReviewsActivityUseCase.ProfessorReviewsActivityUseCase
import com.example.classroom.domain.use_case.evaluations.studentSendActivityUseCase.StudentSendActivityUseCase
import com.example.classroom.domain.use_case.posts.CreatePostUseCase
import com.example.classroom.domain.use_case.posts.DeletePostUseCase
import com.example.classroom.domain.use_case.posts.GetPostsUseCase
import com.example.classroom.domain.use_case.posts.UpdatePostUseCase
import com.example.classroom.domain.use_case.quizz.AnswerQuizzUseCase
import com.example.classroom.domain.use_case.quizz.CreateQuizzUseCase
import com.example.classroom.domain.use_case.signIn.SignInUseCase
import com.example.classroom.domain.use_case.signUp.SignUpUseCase
import com.example.classroom.domain.use_case.validators.ValidatorBundle
import com.example.classroom.domain.use_case.validators.activities.ActivitiesValidator
import com.example.classroom.domain.use_case.validators.cases.ValidateAge
import com.example.classroom.domain.use_case.validators.cases.ValidateDate
import com.example.classroom.domain.use_case.validators.cases.ValidateEmail
import com.example.classroom.domain.use_case.validators.cases.ValidateGrade
import com.example.classroom.domain.use_case.validators.cases.ValidateNames
import com.example.classroom.domain.use_case.validators.cases.ValidatePassword
import com.example.classroom.domain.use_case.validators.cases.ValidatePhone
import com.example.classroom.domain.use_case.validators.cases.ValidateReapeatedPassword
import com.example.classroom.domain.use_case.validators.courses.CoursesValidator
import com.example.classroom.domain.use_case.validators.signIn.SignInValidator
import com.example.classroom.domain.use_case.validators.signUp.SignUpValidator
import com.example.classroom.presentation.screens.Quizz.QuizzViewModel
import com.example.classroom.presentation.screens.activity.ActivityViewmodel
import com.example.classroom.presentation.screens.activity.addActivity.AddActivityViewModel
import com.example.classroom.presentation.screens.activity.studentEvaluations.StudentEvaluationsViewModel
import com.example.classroom.presentation.screens.auth.AuthViewModel
import com.example.classroom.presentation.screens.auth.signIn.SignInViewModel
import com.example.classroom.presentation.screens.chats.ChatViewModel
import com.example.classroom.presentation.screens.course.AddCourse.AddCourseViewModel
import com.example.classroom.presentation.screens.course.CourseViewmodel
import com.example.classroom.presentation.screens.course.posts.PostsViewModel
import com.example.classroom.presentation.screens.course.posts.addPost.AddPostViewModel
import com.example.classroom.presentation.screens.home.HomeViewmodel
import com.example.classroom.presentation.screens.submission.SubmissionViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import proyecto.person.appconsultapopular.data.local.db.AppDatabase


interface AppModule {
    val apiService: ApiService
    val apiClient: HttpClient
    val repositoryBundle: RepositoryBundle
    val signInUseCase: SignInUseCase
    val signUpUseCase: SignUpUseCase
    val updateCourseUseCase: UpdateCourseUseCase
    val insertCourseUseCase: InsertCourseUseCase
    val getCoursesUseCase: GetCoursesUseCase
    val updateActivityUseCase: UpdateActivityUseCase
    val insertActivityUseCase: InsertActivityUseCase
    val getActivitiesUseCase: GetActivitiesUseCase
    val getActivitiesByUserUseCase: GetActivitiesByUserUseCase
    val getCoursesByIdUseCase: GetCoursesByIdUseCase
    val joinUserToCourseUseCase: JoinUserToCourseUseCase
    val joinCourseUseCase: JoinCourseUseCase
    val getUsersByCourseUseCase: GetUsersByCourseUseCase
    val getActivitiesSubmitedByStudent: GetActivitiesSubmitedByStudent
    val professorReviewsActivityUseCase: ProfessorReviewsActivityUseCase
    val studentSendActivityUseCase: StudentSendActivityUseCase
    val deleteCourseUseCase: DeleteCourseUseCase
    val createPostUseCase: CreatePostUseCase
    val updatePostUseCase: UpdatePostUseCase
    val deletePostsUseCase: DeletePostUseCase
    val getPostUseCase: GetPostsUseCase
    val deleteActivityUseCase: DeleteActivityUseCase
    val uploadFileUseCase: UploadFileUseCase
    val createQuizzUseCase: CreateQuizzUseCase
    val answerQuizzUseCase: AnswerQuizzUseCase



    val studentEvaluationsViewModel: StudentEvaluationsViewModel
    val signInViewModel: SignInViewModel
    val homeViewModel: HomeViewmodel
    val addCourseViewModel: AddCourseViewModel
    val activityViewModel: ActivityViewmodel
    val submissionViewModel: SubmissionViewModel
    val courseViewmodel: CourseViewmodel
    val addActivityViewmodel: AddActivityViewModel
    val authViewModel: AuthViewModel
    val addPostViewModel: AddPostViewModel
    val postViewModel: PostsViewModel
    val quizzViewModel: QuizzViewModel
    val chatViewModel: ChatViewModel

    val validatorBundle : ValidatorBundle
    val db: AppDatabase
}

class AppModuleImpl(
    private val appContext: Context,
): AppModule {

    override val apiService: ApiService by lazy {
        ApiServiceImpl(apiClient)
    }

    override val apiClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            expectSuccess = false

            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 60000
            }
        }
    }
    override val repositoryBundle: RepositoryBundle by lazy {
        RepositoryBundle(
            loginRepository = LoginRepositoryImpl(apiService, db.appDao),
            activitiesRepository = ActivitiesRepositoryImpl(apiService, db.appDao),
            coursesRepository = CoursesRepositoryImpl(apiService, db.appDao),
            quizzRepository = QuizzRepositoryImpl(apiService, db.quizDao),
            studentsRepository = StudentsRepositoryImpl(apiService, db.appDao),
            submissionsRepository = SubmissionsRepositoryImpl(apiService, db.appDao),
            postsRepositoryImpl = PostsRepositoryImpl(db.localPostDao, apiService),
            cloudRepository = CloudRepositoryImpl(apiService, db.appDao),
            chatRepository = ChatRepositoryImpl(apiService = apiService,
                chatDao = db.chatDao)

        )
    }
    override val signInUseCase: SignInUseCase by lazy {
        SignInUseCase(repositoryBundle)
    }

    override val signUpUseCase: SignUpUseCase by lazy {
        SignUpUseCase(repositoryBundle)
    }
    override val updateCourseUseCase: UpdateCourseUseCase by lazy {
        UpdateCourseUseCase(repositoryBundle)
    }

    override val insertCourseUseCase: InsertCourseUseCase by lazy {
        InsertCourseUseCase(repositoryBundle)
    }

    override val getCoursesUseCase: GetCoursesUseCase by lazy {
        GetCoursesUseCase(repositoryBundle)
    }

    override val updateActivityUseCase: UpdateActivityUseCase by lazy {
        UpdateActivityUseCase(repositoryBundle)
    }

    override val insertActivityUseCase: InsertActivityUseCase by lazy {
        InsertActivityUseCase(repositoryBundle)
    }

    override val getActivitiesUseCase: GetActivitiesUseCase by lazy {
        GetActivitiesUseCase(repositoryBundle)
    }
    override val getActivitiesByUserUseCase: GetActivitiesByUserUseCase by lazy{
        GetActivitiesByUserUseCase(repositoryBundle)
    }
    override val getCoursesByIdUseCase: GetCoursesByIdUseCase by lazy{
        GetCoursesByIdUseCase(repositoryBundle)
    }
    override val joinUserToCourseUseCase: JoinUserToCourseUseCase by lazy{
        JoinUserToCourseUseCase(repositoryBundle)
    }
    override val joinCourseUseCase: JoinCourseUseCase by lazy{
        JoinCourseUseCase(repositoryBundle)
    }
    override val getUsersByCourseUseCase: GetUsersByCourseUseCase by lazy {
        GetUsersByCourseUseCase(repositoryBundle)
    }
    override val getActivitiesSubmitedByStudent: GetActivitiesSubmitedByStudent by lazy {
        GetActivitiesSubmitedByStudent(repositoryBundle)
    }
    override val professorReviewsActivityUseCase: ProfessorReviewsActivityUseCase by lazy {
        ProfessorReviewsActivityUseCase(repositoryBundle)
    }
    override val studentSendActivityUseCase: StudentSendActivityUseCase by lazy {
        StudentSendActivityUseCase(repositoryBundle)
    }
    override val deleteCourseUseCase: DeleteCourseUseCase by lazy {
        DeleteCourseUseCase(repositoryBundle)
    }
    override val createPostUseCase: CreatePostUseCase by lazy {
        CreatePostUseCase(repositoryBundle)
    }
    override val updatePostUseCase: UpdatePostUseCase by lazy {
        UpdatePostUseCase(repositoryBundle)
    }
    override val deletePostsUseCase: DeletePostUseCase by lazy {
        DeletePostUseCase(repositoryBundle)
    }

    override val getPostUseCase: GetPostsUseCase by lazy {
        GetPostsUseCase(repositoryBundle)
    }
    override val deleteActivityUseCase: DeleteActivityUseCase by lazy {
        DeleteActivityUseCase(repositoryBundle)
    }
    override val uploadFileUseCase: UploadFileUseCase by lazy {
        UploadFileUseCase(repositoryBundle)
    }
    override val createQuizzUseCase: CreateQuizzUseCase by lazy {
        CreateQuizzUseCase(repositoryBundle)
    }
    override val answerQuizzUseCase: AnswerQuizzUseCase by lazy {
        AnswerQuizzUseCase(repositoryBundle)
    }

    override val studentEvaluationsViewModel: StudentEvaluationsViewModel by lazy {
        StudentEvaluationsViewModel(
            repositoryBundle,
            getActivitiesSubmitedByStudent,
        )
    }
    override val signInViewModel: SignInViewModel by lazy {
        SignInViewModel(
            signInUseCase = signInUseCase,
            loginRepositoryImp = LoginRepositoryImpl(apiService, db.appDao)
        )
    }

    override val homeViewModel: HomeViewmodel by lazy {
        HomeViewmodel(
            repositoryBundle = repositoryBundle,
            getCoursesUseCase = getCoursesUseCase,
            joinCourseUseCase = joinCourseUseCase,
            deleteCourseUseCase = deleteCourseUseCase,
            notificationDao = db.notificationDao
        )
    }

    override val addCourseViewModel: AddCourseViewModel by lazy {
        AddCourseViewModel(
            insertCourseUseCase = insertCourseUseCase,
            updateCourseUseCase = updateCourseUseCase,
            repositoryBundle = repositoryBundle
        )
    }

    override val activityViewModel: ActivityViewmodel by lazy {
        ActivityViewmodel(
            activityDataValidator = ActivityDataValidator(),
            insertActivityUseCase = insertActivityUseCase,
            repositoryBundle = repositoryBundle,
            updateActivityUseCase = updateActivityUseCase,
            getActivitiesUseCase = getActivitiesUseCase,
            activitiesValidator = validatorBundle.activitiesValidator,
            getActivitiesByUserUseCase = getActivitiesByUserUseCase,
            deleteActivityUseCase = deleteActivityUseCase,
        )
    }

    override val submissionViewModel: SubmissionViewModel by lazy {
        SubmissionViewModel(
            repositoryBundle = repositoryBundle,
            professorReviewsActivityUseCase = professorReviewsActivityUseCase,
            studentSendActivityUseCase = studentSendActivityUseCase,
            uploadFileUseCase = uploadFileUseCase,

        )
    }
    override val courseViewmodel: CourseViewmodel by lazy {
        CourseViewmodel(
            repositoryBundle = repositoryBundle,
            courseDataValidator = CourseDataValidator(),
            getActivitiesUseCase = getActivitiesUseCase,
            insertCourseUseCase = insertCourseUseCase,
            updateCourseUseCase = updateCourseUseCase,
            coursesValidator = validatorBundle.coursesValidator,
            getCoursesByIdUseCase = getCoursesByIdUseCase,
            joinUserToCourseUseCase = joinUserToCourseUseCase,
            getUsersByCourseUseCase = getUsersByCourseUseCase,
            getActivitiesSubmitedByStudent = getActivitiesSubmitedByStudent
        )
    }
    override val addActivityViewmodel: AddActivityViewModel by lazy {
      AddActivityViewModel(
          updateActivityUseCase = updateActivityUseCase,
          insertActivityUseCase = insertActivityUseCase,
          repositoryBundle = repositoryBundle
      )
    }
    override val authViewModel: AuthViewModel by lazy {
        AuthViewModel(
            loginRepositoryImp = LoginRepositoryImpl(
                apiService,
                dao = db.appDao
            ),
            signInUseCase = signInUseCase,
            signInValidator = validatorBundle.signInValidator,
            signUpUseCase = signUpUseCase,
            signUpValidator = validatorBundle.signUpValidator,
            userDataValidator = UserDataValidator(),
        )
    }
    override val addPostViewModel: AddPostViewModel by lazy {
        AddPostViewModel(
            repositoryBundle = repositoryBundle,
            createPostUseCase = createPostUseCase,
            updatePostUseCase = updatePostUseCase,
            uploadFileUseCase= uploadFileUseCase
        )
    }
    override val postViewModel: PostsViewModel by lazy {
        PostsViewModel(
           repository = repositoryBundle.postsRepositoryImpl,
            getPostsUseCase = getPostUseCase,
            deletePostUseCase = deletePostsUseCase,
            repositoryBundle = repositoryBundle
        )
    }
    override val quizzViewModel: QuizzViewModel by lazy {
        QuizzViewModel(
            repositoryBundle = repositoryBundle,
            answerQuizzUseCase = answerQuizzUseCase,
            createQuizzUseCase = createQuizzUseCase
        )
    }
    override val chatViewModel: ChatViewModel by lazy {
        ChatViewModel(
            repositoryBundle = repositoryBundle,
        )
    }

    override val validatorBundle: ValidatorBundle by lazy {
                ValidatorBundle(
                    coursesValidator = CoursesValidator(
                        ValidateNames()
                    ),
                    signInValidator = SignInValidator(
                        ValidateEmail(),
                        ValidatePassword(),
                    ),
                    signUpValidator = SignUpValidator(
                        ValidateNames(),
                        ValidatePassword(),
                        ValidateEmail(),
                        ValidateReapeatedPassword(),
                        ValidateAge(),
                        ValidatePhone()
                    ),
                    activitiesValidator = ActivitiesValidator(
                        ValidateNames(),
                        ValidateDate(),
                        ValidateGrade()
                    )
                )
    }

    override val db: AppDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigrationFrom(1,2).build()
    }



}