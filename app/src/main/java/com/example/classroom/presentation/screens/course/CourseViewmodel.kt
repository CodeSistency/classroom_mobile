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
import com.example.classroom.domain.model.entity.LocalActivitySubmission
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
import com.example.classroom.presentation.screens.course.states.DeleteStudentState
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

    private val _listActivitiesSubmittedFlow = MutableStateFlow<List<LocalActivitySubmission>>(emptyList())
    val listActivitiesSubmittedFlow: StateFlow<List<LocalActivitySubmission>> = _listActivitiesSubmittedFlow

    private val _filteredListActivitiesSubmittedFlow = MutableStateFlow<List<LocalActivitySubmission>>(emptyList())
    val filteredListActivitiesSubmittedFlow: StateFlow<List<LocalActivitySubmission>> = _filteredListActivitiesSubmittedFlow


    private val _listActivitiesFlow = MutableStateFlow<List<LocalActivities>>(emptyList())
    val listActivitiesFlow: StateFlow<List<LocalActivities>> = _listActivitiesFlow

    val studentInput = MutableStateFlow("")
    val postInput = MutableStateFlow("")
    val activityInput = MutableStateFlow("")
    val activitySubmittedInput = MutableStateFlow("")


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

    private val _stateDeleteStudents = MutableStateFlow(DeleteStudentState())
    val stateDeleteStudents: StateFlow<DeleteStudentState> = _stateDeleteStudents

    init {
        viewModelScope.launch {
            repositoryBundle.loginRepository.getUserInfoWithFlow()
                .firstOrNull()
                ?.firstOrNull()
                ?.let { _userInfo.value = it }

//            observeUserInput()

            _listActivitiesFlow.value = repositoryBundle.activitiesRepository.getActivitiesWithFlow().first()
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
        _filteredListUsersByCourseFlow.value = if (query.isBlank()) {
            _listStudentsFlow.value
        } else {
            _listStudentsFlow.value.filter { student ->
                student.name.contains(query, ignoreCase = true) ||
                        student.lastname.contains(query, ignoreCase = true)
            }
        }
    }

    fun filterActivitiesSubmitted(query: String) {
        _filteredListActivitiesSubmittedFlow.value = if (query.isBlank()) {
            _listActivitiesSubmittedFlow.value
        } else {
            _listActivitiesSubmittedFlow.value.filter { activity ->
                activity.activityId.contains(query, ignoreCase = true)
//                        || student.lastname.contains(query, ignoreCase = true)
            }
        }
    }

    suspend fun getActivitiesSubmitted(activityId: String, studentId: String) {
        try {
            _filteredListActivitiesSubmittedFlow.value = repositoryBundle.submissionsRepository.getSubmissionsForStudent(activityId, studentId).first()

            Log.e("_filteredListActivitiesSubmittedFlow", _filteredListActivitiesSubmittedFlow.value.toString())
        } catch (e: Exception) {
            e.printStackTrace()
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
                .getSubmissionsForStudentByCourse(courseId, studentId)
//                .distinctUntilChanged() // Only emit new data if it's actually different
                .collect { evaluations ->
                    Log.e("observeLocalEvaluations", "Received local data: $evaluations")

                    if (!evaluations.isNullOrEmpty()) {
                        // Only update if evaluations is non-null and non-empty
                        _listActivitiesSubmittedFlow.value = evaluations
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            info = evaluations,
                            isLoading = false,
                            error = null
                        )
                    }else{
                        _listActivitiesSubmittedFlow.value = emptyList()
                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
                            info = emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun getLocalEvaluations(courseId: String, studentId: String) {
        Log.e("submitted", "courseid ${courseId} studentId ${studentId}")
        viewModelScope.launch {
            repositoryBundle.submissionsRepository.getSubmissionsForStudentByCourse(studentId, courseId).collect {
                Log.e("evaluaciones", _listActivitiesFlow.value.toString())
                _listActivitiesSubmittedFlow.value = it
            }

        }
//        viewModelScope.launch {
//            repositoryBundle.submissionsRepository
//                .getSubmissionsForStudentByCourse(courseId, studentId)
////                .distinctUntilChanged() // Only emit new data if it's actually different
//                .collect { evaluations ->
//                    Log.e("observeLocalEvaluations", "Received local data: $evaluations")
//
//                    if (!evaluations.isNullOrEmpty()) {
//                        // Only update if evaluations is non-null and non-empty
//                        _listActivitiesSubmittedFlow.value = evaluations
//                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
//                            info = evaluations,
//                            isLoading = false,
//                            error = null
//                        )
//                    }else{
//                        _listActivitiesSubmittedFlow.value = emptyList()
//                        _stateStudentEvaluations.value = _stateStudentEvaluations.value.copy(
//                            info = emptyList(),
//                            isLoading = false,
//                            error = null
//                        )
//                    }
//                }
//        }
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
                            Log.e("evaluaciones", evaluations.toString())
                            repositoryBundle.submissionsRepository.addSubmissionsWithoutDuplicates(evaluations)
                            delay(300)
                            getLocalEvaluations(studentId = userId, courseId = courseId)
//                            observeLocalEvaluations(studentId = userId, courseId = courseId)
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

