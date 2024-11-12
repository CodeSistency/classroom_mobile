package com.example.classroom.presentation.screens.course

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.common.validator.CourseDataValidator
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.use_case.activities.GetActivitiesUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesByIdUseCase
import com.example.classroom.domain.use_case.courses.GetUsersByCourseUseCase
import com.example.classroom.domain.use_case.courses.InsertCourseUseCase
import com.example.classroom.domain.use_case.courses.JoinUserToCourseUseCase
import com.example.classroom.domain.use_case.courses.UpdateCourseUseCase
import com.example.classroom.domain.use_case.evaluations.getActivitiesSubmittedByStudent.GetActivitiesSubmitedByStudent
import com.example.classroom.domain.use_case.validators.courses.CoursesValidator
import com.example.classroom.presentation.screens.activity.studentEvaluations.states.StudentEvaluationsState
import com.example.classroom.presentation.screens.course.AddCourse.states.AddCourseState
import com.example.classroom.presentation.screens.course.AddCourse.CourseFormEvent
import com.example.classroom.presentation.screens.course.AddCourse.states.CourseFormState
import com.example.classroom.presentation.screens.course.states.GetCourseState
import com.example.classroom.presentation.screens.course.states.GetUsersState
import com.example.classroom.presentation.screens.course.states.JoinUserState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class CourseViewmodel(
    private val coursesValidator: CoursesValidator,
    private val courseDataValidator: CourseDataValidator,
    private val insertCourseUseCase: InsertCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val getCoursesByIdUseCase: GetCoursesByIdUseCase,
    private val joinUserToCourseUseCase: JoinUserToCourseUseCase,
    private val getUsersByCourseUseCase: GetUsersByCourseUseCase,
    private val getActivitiesSubmitedByStudent: GetActivitiesSubmitedByStudent,
    private val repositoryBundle: RepositoryBundle
) : ViewModel() {

    // User information and ownership status
    private val _userInfo = MutableStateFlow<LocalUser?>(null)
    val userInfo: StateFlow<LocalUser?> = _userInfo

    private val _isOwner = MutableStateFlow<Boolean?>(null)
    val isOwner: StateFlow<Boolean?> = _isOwner

    // Course and students data
    private val _courseFlow = MutableStateFlow<LocalCourses?>(null)
    val courseFlow: StateFlow<LocalCourses?> = _courseFlow

    private val _listStudentsFlow = MutableStateFlow<List<LocalStudents>>(emptyList())
    val listStudentsFlow: StateFlow<List<LocalStudents>> = _listStudentsFlow

    private val _filteredListUsersByCourseFlow = MutableStateFlow<List<LocalStudents>>(emptyList())
    val filteredListUsersByCourseFlow: StateFlow<List<LocalStudents>> = _filteredListUsersByCourseFlow

    // Form input states
    val userInput = MutableStateFlow("")
    val titleField = MutableStateFlow("")
    val descripcionField = MutableStateFlow("")
    val seccionField = MutableStateFlow("")
    val subjectField = MutableStateFlow("")
    val areaField = MutableStateFlow(Area.OTHER)

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    // State management for course form and network calls
    private val _stateCourseForm = MutableStateFlow(CourseFormState())
    val stateCourseForm: StateFlow<CourseFormState> = _stateCourseForm

    private val _stateCourse = MutableStateFlow(AddCourseState())
    val stateCourse: StateFlow<AddCourseState> = _stateCourse

    private val _stateGetCourse = MutableStateFlow(GetCourseState())
    val stateGetCourse: StateFlow<GetCourseState> = _stateGetCourse

    private val _stateGetUsers = MutableStateFlow(GetUsersState())
    val stateGetUsers: StateFlow<GetUsersState> = _stateGetUsers

    private val _stateJoinUser = MutableStateFlow(JoinUserState())
    val stateJoinUser: StateFlow<JoinUserState> = _stateJoinUser

    init {
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { _userInfo.value = it }

//            observeUserInput()
        }
    }

    private fun observeUserInput() {
        // Filtering students by userInput
        viewModelScope.launch {
            combine(_listStudentsFlow, userInput) { students, input ->
                if (input.isEmpty()) students else students.filter { it.name.startsWith(input, ignoreCase = true) }
            }.collectLatest {
                _filteredListUsersByCourseFlow.value = it
            }
        }
    }

    suspend fun getUsersByCourseRemote(id: String) {
        getUsersByCourseUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _stateGetUsers.value = GetUsersState(error = result.message)
                is Resource.Loading -> _stateGetUsers.value = GetUsersState(isLoading = true)
                is Resource.Success -> {
                    _stateGetUsers.value = GetUsersState(info = result.data)
                    result.data?.let { repositoryBundle.studentsRepository.addStudentsToCourse(id, it) }
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getUsersByCourseLocal(id: String) {
        try {
            // Retrieve students data from the repository
            val students = repositoryBundle.studentsRepository.getStudentsByCourseId(id).first()

            // Log the retrieved students data
            Log.e("students", "Retrieved students from repository: $students")

            // Update _listStudentsFlow and _filteredListUsersByCourseFlow
            _listStudentsFlow.value = students
            _filteredListUsersByCourseFlow.value = students // Initialize with the full list

            // Log the flows to confirm they’ve been updated
            Log.e("students", "_listStudentsFlow updated: ${_listStudentsFlow.value}")
            Log.e("students", "_filteredListUsersByCourseFlow updated: ${_filteredListUsersByCourseFlow.value}")
        } catch (e: Exception) {
            // Log exception details for debugging
            Log.e("students", "Error retrieving students: ${e.message}")
            e.printStackTrace()
        }
    }


    // Function to filter students based on search query
    fun filterStudents(query: String) {
        if (query.isBlank()) {
            // Show full list if query is empty
            _filteredListUsersByCourseFlow.value = _listStudentsFlow.value
        } else {
            // Filter list based on query
            _filteredListUsersByCourseFlow.value = _listStudentsFlow.value.filter { student ->
                student.name.contains(query, ignoreCase = true) ||
                        student.lastname.contains(query, ignoreCase = true)
            }
        }
    }

    suspend fun getCourseByIdLocal(id: String) {
        try {
            _courseFlow.value = repositoryBundle.coursesRepository.getCoursesWithFlowById(id).first()
            Log.e("courseFlow func", repositoryBundle.coursesRepository.getCoursesWithFlowById(id).first().toString())

            Log.e("courseFlow", _courseFlow.value.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCourseById(id: String) {
        getCoursesByIdUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> _stateGetCourse.value = GetCourseState(error = result.message)
                is Resource.Loading -> _stateGetCourse.value = GetCourseState(isLoading = true)
                is Resource.Success -> {
                    _stateGetCourse.value = GetCourseState(info = result.data)

                    //Esto hay que descomentarlo
//                    _courseFlow.value = result.data
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onCourseEvent(event: CourseFormEvent) {
        when (event) {
            is CourseFormEvent.AreaChanged -> _stateCourseForm.value = _stateCourseForm.value.copy(area = event.area)
            is CourseFormEvent.DescriptionChanged -> _stateCourseForm.value = _stateCourseForm.value.copy(description = event.description)
            is CourseFormEvent.SectionChanged -> _stateCourseForm.value = _stateCourseForm.value.copy(section = event.section)
            is CourseFormEvent.SubjectChanged -> _stateCourseForm.value = _stateCourseForm.value.copy(subject = event.subject)
            is CourseFormEvent.TitleChanged -> _stateCourseForm.value = _stateCourseForm.value.copy(title = event.title)
            is CourseFormEvent.Submit -> submitCourseData(event.id, event.body)
        }
    }

    private fun submitCourseData(id: String?, course: CourseRequestDto) {
        val hasError = listOf(
            coursesValidator.validateNames.execute(_stateCourseForm.value.title),
            coursesValidator.validateNames.execute(_stateCourseForm.value.description),
            coursesValidator.validateNames.execute(_stateCourseForm.value.section),
            coursesValidator.validateNames.execute(_stateCourseForm.value.subject)
        ).any { !it.successful }

        if (hasError) {
            _stateCourseForm.value = _stateCourseForm.value.copy(
                subjectError = "Subject error",
                sectionError = "Section error",
                titleError = "Title error",
                descriptionError = "Description error"
            )
            return
        }

        viewModelScope.launch {
            executeCourseRequest(course, id)
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    private suspend fun executeCourseRequest(courseRequestDto: CourseRequestDto, id: String?) {
        val resultFlow = if (id != null) {
            updateCourseUseCase(courseRequestDto, id)
        } else {
            insertCourseUseCase(courseRequestDto)
        }

        resultFlow.onEach { result ->
            when (result) {
                is Resource.Error -> _stateCourse.value = AddCourseState(error = result.message)
                is Resource.Loading -> _stateCourse.value = AddCourseState(isLoading = true)
                is Resource.Success -> {
                    _stateCourse.value = AddCourseState(info = result.data)
                    result.data?.let { repositoryBundle.coursesRepository.insertCourse(it) }
                }
            }
        }.launchIn(viewModelScope)
    }

    suspend fun joinUser(id: String, token: String) {
        joinUserToCourseUseCase(id, token).onEach { result ->
            when (result) {
                is Resource.Error -> _stateJoinUser.value = JoinUserState(error = result.message)
                is Resource.Loading -> _stateJoinUser.value = JoinUserState(isLoading = true)
                is Resource.Success -> _stateJoinUser.value = JoinUserState(info = result.data)
            }
        }.launchIn(viewModelScope)
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }


    private val _stateStudentEvaluations = mutableStateOf(StudentEvaluationsState())
    val stateStudentEvaluations: State<StudentEvaluationsState> = _stateStudentEvaluations

    // Load evaluations from the local database only
    fun observeLocalEvaluations(courseId: String, studentId: String) {
        Log.e("submitted", "courseid ${courseId} studentId ${studentId}")
        viewModelScope.launch {
            repositoryBundle.submissionsRepository
                .getSubmissionsForStudentAndCourse(courseId, studentId)
                .distinctUntilChanged() // Only emit new data if it's actually different
                .collect { evaluations ->
                    Log.e("observeLocalEvaluations", "Received local data: $evaluations")

                    if (!evaluations.isNullOrEmpty()) {
                        // Only update if evaluations is non-null and non-empty
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            info = evaluations,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }
    // Fetch evaluations remotely and sync with the local database
    fun getActivitiesByStudent(courseId: String, userId: String) {
        viewModelScope.launch {
            getActivitiesSubmitedByStudent(courseId, userId).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        Log.e("getActivitiesByStudent", "Error loading remote data")
                        // Preserve `info` if it already has data to prevent UI from clearing
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            error = result.message,
                            isLoading = false,
                            info = _stateStudentEvaluations.value.info // Keep existing data
                        )
                    }
                    is Resource.Loading -> {
                        Log.e("getActivitiesByStudent", "Loading remote data")
                        // Only set loading without resetting info
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            isLoading = true,
                            info = _stateStudentEvaluations.value.info // Keep existing data
                        )
                    }
                    is Resource.Success -> {
                        Log.e("getActivitiesByStudent", "Successfully fetched remote data")
                        result.data?.let { evaluations ->
                            // Insert the data into the local database
//                            repositoryBundle.submissionsRepository.insertAllSubmissions(evaluations)
                        }
                        // Reset `isLoading` and error without clearing `info`
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }
}


//class CourseViewmodel(
//    private val coursesValidator: CoursesValidator,
//    private val courseDataValidator: CourseDataValidator,
//    private val insertCourseUseCase: InsertCourseUseCase,
//    private val updateCourseUseCase: UpdateCourseUseCase,
//    private val getActivitiesUseCase: GetActivitiesUseCase,
//    private val getCoursesByIdUseCase: GetCoursesByIdUseCase,
//    private val joinUserToCourseUseCase: JoinUserToCourseUseCase,
//    private val getUsersByCourseUseCase: GetUsersByCourseUseCase,
//    private val repositoryBundle: RepositoryBundle,
////    private val insertActivityUseCase: InsertActivityUseCase,
////    private val updateActivityUseCase: UpdateActivityUseCase,
////    private val sendActivityUseCase: SendActivityUseCase,
////    private val deleteActivityUseCase: DeleteActivityUseCase
//): ViewModel() {
//
//    var userInfo: Flow<LocalUser?> = emptyFlow()
//    var isOwner: Flow<Boolean?> = emptyFlow()
//
//    var courseFlow: Flow<LocalCourses?> = emptyFlow()
//
//    var filteredListUsersByCourseFLow: Flow<List<LocalStudents>> = emptyFlow()
//    var listStudensFlow: Flow<List<LocalStudents>> = emptyFlow()
//
//    var stateCourseForm by mutableStateOf(CourseFormState())
//
//    private val _stateCourse = mutableStateOf(AddCourseState())
//    val stateCourse: State<AddCourseState> = _stateCourse
//
//    private val _stateGetCourse = mutableStateOf(GetCourseState())
//    val stateGetCourse: State<GetCourseState> = _stateGetCourse
//
//    private val _stateGetUsers = mutableStateOf(GetUsersState())
//    val stateGetUsers: State<GetUsersState> = _stateGetUsers
//
//    private val _stateJoinUser = mutableStateOf(JoinUserState())
//    val stateJoinUser: State<JoinUserState> = _stateJoinUser
//
//    val userInput = mutableStateOf("")
//
//    init {
//        viewModelScope.launch {
//
//            filteredListUsersByCourseFLow = if (userInput.value.isNotBlank()){
//                listStudensFlow.map { list ->
//                    list.filter { it.name.startsWith(userInput.value) }
//                        .sortedByDescending { activity -> activity.id }
//                }
//            }else{
//                listStudensFlow
//            }
//        }
//    }
//
//    val titleField = mutableStateOf("")
//    val descripcionField = mutableStateOf("")
//    val seccionField = mutableStateOf("")
//    val subjectField = mutableStateOf("")
//    val areaField = mutableStateOf(Area.OTHER)
//
//    private val validationEventChannel = Channel<ValidationEvent>()
//    val validationEvents = validationEventChannel.receiveAsFlow()
//
//    init {
//        viewModelScope.launch {
//            userInfo = repositoryBundle.loginRepository.getUserInfoWithFlow().let {
//                it?.first().let {
//                    flow { it }
//                }
//            }
////            isOwner = if (courseFlow.first().let { if (it != null) it.owner } == userInfo.first().let { if (it != null) it.idApi }){
////                flow {  true  }
////            }else{
////                flow {  false  }
////
////            }
//        }
//    }
//
//    suspend fun getUsersByCourseRemote(id: String){
//        getUsersByCourseUseCase(id).onEach { result ->
//            when(result){
//                is Resource.Error -> {
//                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                    Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                    _stateGetUsers.value = GetUsersState(error = result.message)
//                }
//                is Resource.Loading -> {
//                    Timber.tag("ACTIVITIES").e("is loading")
//                    _stateGetUsers.value = GetUsersState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    Timber.tag("ACTIVITIES_VM").e("success")
//                    Log.e("ACTIVITIES:", "success")
//                    _stateGetUsers.value = GetUsersState(info = result.data)
//                    Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
//                    _stateGetUsers.value.info?.let {
//                        repositoryBundle.studentsRepository.addStudentsToCourse(id, it)
////                        repositoryBundle.coursesRepository.updateUsersInCourse(it, id)
//                        delay(300)
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
//
//    }
//
//   suspend fun getUsersByCourseLocal(id: String){
//       listStudensFlow = repositoryBundle.studentsRepository.getStudentsByCourseId(id)
//        Log.e("students", listStudensFlow.first().toString())
//       filteredListUsersByCourseFLow = if (userInput.value.isNotBlank()){
//           listStudensFlow.map { list ->
//               list.filter { it.name.startsWith(userInput.value) }
//                   .sortedByDescending { activity -> activity.id }
//           }
//       }else{
//           listStudensFlow
//       }
//   }
//
//    suspend fun getCourseByIdLocal(id: String){
//        courseFlow = repositoryBundle.coursesRepository.getCoursesWithFlowById(id)
//    }
//
//    suspend fun getCourseById(id: String){
//        getCoursesByIdUseCase(id).onEach { result ->
//            when(result){
//                is Resource.Error -> {
//                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                    Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                    _stateGetCourse.value = GetCourseState(error = result.message)
//                }
//                is Resource.Loading -> {
//                    Timber.tag("ACTIVITIES").e("is loading")
//                    _stateGetCourse.value = GetCourseState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    Timber.tag("ACTIVITIES_VM").e("success")
//                    Log.e("ACTIVITIES:", "success")
//                    _stateGetCourse.value = GetCourseState(info = result.data)
//                    Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
//                    _stateGetCourse.value.info?.let {
////                            insertUserDb(it)
//                        delay(300)
//
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
//
//    }
//    fun onCourseEvent(event: CourseFormEvent) {
//        when(event) {
//
//            is CourseFormEvent.AreaChanged -> {
//                stateCourseForm = stateCourseForm.copy(area = event.area)
//             }
//            is CourseFormEvent.DescriptionChanged -> {
//                stateCourseForm = stateCourseForm.copy(description = event.description)
//            }
//            is CourseFormEvent.SectionChanged -> {
//                stateCourseForm = stateCourseForm.copy(section = event.section)
//            }
//            is CourseFormEvent.SubjectChanged -> {
//                stateCourseForm = stateCourseForm.copy(subject = event.subject)
//            }
//            is CourseFormEvent.TitleChanged -> {
//                stateCourseForm = stateCourseForm.copy(title = event.title)
//            }
//            is CourseFormEvent.Submit -> {
//                submitCourseData(event.id, event.body)
//            }
////            is CourseFormEvent.Submit -> submitCourseData(id = event.id)
//
//        }
//    }
//
//
//    private fun submitCourseData(id: String?, course: CourseRequestDto) {
//        val titleResult = coursesValidator.validateNames.execute(stateCourseForm.title)
//        val descriptionResult = coursesValidator.validateNames.execute(stateCourseForm.description)
//        val sectionResult = coursesValidator.validateNames.execute(stateCourseForm.section)
//        val subjectResult = coursesValidator.validateNames.execute(stateCourseForm.subject)
//
//        val hasError = listOf(
//            titleResult,
//            descriptionResult,
//            sectionResult,
//            subjectResult,
//
//        ).any { !it.successful }
//
//        if(hasError) {
//            stateCourseForm = stateCourseForm.copy(
//                subjectError = subjectResult.errorMessage,
//                sectionError = sectionResult.errorMessage,
//                titleError = titleResult.errorMessage,
//                descriptionError = descriptionResult.errorMessage,
//                )
//            return
//        }
//        Log.e("final", "final")
//        viewModelScope.launch {
//            Log.e("final2", "final2")
//            executeCourseRequest(course, id)
//            validationEventChannel.send(ValidationEvent.Success)
//        }
//    }
//
//    suspend fun executeCourseRequest(courseRequestDto: CourseRequestDto, id: String?){
//
//        if (id != null){
//            updateCourseUseCase(courseRequestDto, id).onEach { result ->
//                when(result){
//                    is Resource.Error -> {
//                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                        _stateCourse.value = AddCourseState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        Timber.tag("ACTIVITIES").e("is loading")
//                        _stateCourse.value = AddCourseState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        Timber.tag("ACTIVITIES_VM").e("success")
//                        Log.e("ACTIVITIES:", "success")
//                        _stateCourse.value = AddCourseState(info = result.data)
//                        Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
//                        _stateCourse.value.info?.let {
//                            repositoryBundle.coursesRepository.insertCourse(it)
////                            insertUserDb(it)
//                            delay(300)
//
//                        }
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }else {
//            insertCourseUseCase(courseRequestDto).onEach { result ->
//                when(result){
//                    is Resource.Error -> {
//                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                        _stateCourse.value = AddCourseState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        Timber.tag("ACTIVITIES").e("is loading")
//                        _stateCourse.value = AddCourseState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        Timber.tag("ACTIVITIES_VM").e("success")
//                        Log.e("ACTIVITIES:", "success")
//                        _stateCourse.value = AddCourseState(info = result.data)
//                        Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
//                        _stateCourse.value.info?.let {
////                            insertUserDb(it)
//                            delay(300)
//
//                        }
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }
//
//    }
//
//    suspend fun joinUser(id: String, token: String){
//        joinUserToCourseUseCase(id, token).onEach { result ->
//            when(result){
//                is Resource.Error -> {
//                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                    Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
//                    _stateJoinUser.value = JoinUserState(error = result.message)
//                }
//                is Resource.Loading -> {
//                    Timber.tag("ACTIVITIES").e("is loading")
//                    _stateJoinUser.value = JoinUserState(isLoading = true)
//                }
//                is Resource.Success -> {
//                    Timber.tag("ACTIVITIES_VM").e("success")
//                    Log.e("ACTIVITIES:", "success")
//                    _stateJoinUser.value = JoinUserState(info = result.data)
//                    Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
//                    _stateJoinUser.value.info?.let {
////                            insertUserDb(it)
//                        delay(300)
//                    }
//                }
//            }
//        }.launchIn(viewModelScope)
//    }
//
//
//    sealed class ValidationEvent {
//        object Success: ValidationEvent()
//    }
//}