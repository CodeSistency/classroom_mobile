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
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.model.entity.toCoursesLocal
import com.example.classroom.domain.use_case.activities.GetActivitiesUseCase
import com.example.classroom.domain.use_case.courses.GetCoursesByIdUseCase
import com.example.classroom.domain.use_case.courses.InsertCourseUseCase
import com.example.classroom.domain.use_case.courses.UpdateCourseUseCase
import com.example.classroom.domain.use_case.validators.courses.CoursesValidator
import com.example.classroom.presentation.screens.course.AddCourse.states.AddCourseState
import com.example.classroom.presentation.screens.course.AddCourse.CourseFormEvent
import com.example.classroom.presentation.screens.course.AddCourse.states.CourseFormState
import com.example.classroom.presentation.screens.course.states.GetCourseState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
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
    private val repositoryBundle: RepositoryBundle,
//    private val insertActivityUseCase: InsertActivityUseCase,
//    private val updateActivityUseCase: UpdateActivityUseCase,
//    private val sendActivityUseCase: SendActivityUseCase,
//    private val deleteActivityUseCase: DeleteActivityUseCase
): ViewModel() {

    var userInfo: Flow<LocalUser?> = emptyFlow()
    var isOwner: Flow<Boolean?> = emptyFlow()

    var courseFlow: Flow<LocalCourses?> = emptyFlow()
    var stateCourseForm by mutableStateOf(CourseFormState())

    private val _stateCourse = mutableStateOf(AddCourseState())
    val stateCourse: State<AddCourseState> = _stateCourse

    private val _stateGetCourse = mutableStateOf(GetCourseState())
    val stateGetCourse: State<GetCourseState> = _stateGetCourse

    val titleField = mutableStateOf("")
    val descripcionField = mutableStateOf("")
    val seccionField = mutableStateOf("")
    val subjectField = mutableStateOf("")
    val areaField = mutableStateOf(Area.OTHER)

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            userInfo = repositoryBundle.loginRepository.getUserLogged()
//            isOwner = if (courseFlow.first().let { if (it != null) it.owner } == userInfo.first().let { if (it != null) it.idApi }){
//                flow {  true  }
//            }else{
//                flow {  false  }
//
//            }
        }
    }

    suspend fun isOwnerCourse(){
        isOwner = if (courseFlow.first().let { if (it != null) it.owner } == userInfo.first().let { if (it != null) it.idApi }){
            flow {  true  }
        }else{
            flow {  false  }

        }
    }
    suspend fun getUserLogged(){
        userInfo = repositoryBundle.loginRepository.getUserLogged()
    }
    suspend fun getCourseByIdLocal(id: String){
        courseFlow = repositoryBundle.coursesRepository.getCoursesWithFlowById(id)
    }

    suspend fun getCourseById(id: String){
        getCoursesByIdUseCase(id).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                    _stateGetCourse.value = GetCourseState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("ACTIVITIES").e("is loading")
                    _stateGetCourse.value = GetCourseState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("ACTIVITIES_VM").e("success")
                    Log.e("ACTIVITIES:", "success")
                    _stateGetCourse.value = GetCourseState(info = result.data)
                    Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
                    _stateGetCourse.value.info?.let {
//                            insertUserDb(it)
                        delay(300)

                    }
                }
            }
        }.launchIn(viewModelScope)

    }
    fun onActivityEvent(event: CourseFormEvent) {
        when(event) {

            is CourseFormEvent.AreaChanged -> {
                stateCourseForm = stateCourseForm.copy(area = event.area)
             }
            is CourseFormEvent.DescriptionChanged -> {
                stateCourseForm = stateCourseForm.copy(description = event.description)
            }
            is CourseFormEvent.SectionChanged -> {
                stateCourseForm = stateCourseForm.copy(section = event.section)
            }
            is CourseFormEvent.SubjectChanged -> {
                stateCourseForm = stateCourseForm.copy(subject = event.subject)
            }
            is CourseFormEvent.TitleChanged -> {
                stateCourseForm = stateCourseForm.copy(title = event.title)
            }
            is CourseFormEvent.Submit -> {
                submitCourseData(event.id, event.body)
            }
//            is CourseFormEvent.Submit -> submitCourseData(id = event.id)

        }
    }


    private fun submitCourseData(id: String?, course: CourseRequestDto) {
        val titleResult = coursesValidator.validateNames.execute(stateCourseForm.title)
        val descriptionResult = coursesValidator.validateNames.execute(stateCourseForm.description)
        val sectionResult = coursesValidator.validateNames.execute(stateCourseForm.section)
        val subjectResult = coursesValidator.validateNames.execute(stateCourseForm.subject)

        val hasError = listOf(
            titleResult,
            descriptionResult,
            sectionResult,
            subjectResult,

        ).any { !it.successful }

        if(hasError) {
            stateCourseForm = stateCourseForm.copy(
                subjectError = subjectResult.errorMessage,
                sectionError = sectionResult.errorMessage,
                titleError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                )
            return
        }
        Log.e("final", "final")
        viewModelScope.launch {
            Log.e("final2", "final2")
            executeCourseRequest(course, id)
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    suspend fun executeCourseRequest(courseRequestDto: CourseRequestDto, id: String?){

        if (id != null){
            updateCourseUseCase(courseRequestDto, id).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                        _stateCourse.value = AddCourseState(error = result.message)
                    }
                    is Resource.Loading -> {
                        Timber.tag("ACTIVITIES").e("is loading")
                        _stateCourse.value = AddCourseState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Timber.tag("ACTIVITIES_VM").e("success")
                        Log.e("ACTIVITIES:", "success")
                        _stateCourse.value = AddCourseState(info = result.data)
                        Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
                        _stateCourse.value.info?.let {
//                            insertUserDb(it)
                            delay(300)

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }else {
            insertCourseUseCase(courseRequestDto).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                        _stateCourse.value = AddCourseState(error = result.message)
                    }
                    is Resource.Loading -> {
                        Timber.tag("ACTIVITIES").e("is loading")
                        _stateCourse.value = AddCourseState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Timber.tag("ACTIVITIES_VM").e("success")
                        Log.e("ACTIVITIES:", "success")
                        _stateCourse.value = AddCourseState(info = result.data)
                        Log.e("ACTIVITIES:", "${_stateCourse.value.info}")
                        _stateCourse.value.info?.let {
//                            insertUserDb(it)
                            delay(300)

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    suspend fun onCreateCourseClick(course: CourseRequestDto, id: String?){
        val isValid = mutableStateOf(false)

        when (val result = courseDataValidator.validateTitle(course.title)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    CourseDataValidator.InputError.TOO_SHORT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }
        }

        when (val result = courseDataValidator.validateTitle(course.title)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    CourseDataValidator.InputError.TOO_SHORT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }
        }

        when (val result = courseDataValidator.validateSection(course.section)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    CourseDataValidator.InputError.TOO_SHORT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }
        }

        when (val result = courseDataValidator.validateSubject(course.subject)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    CourseDataValidator.InputError.TOO_SHORT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }
        }

        when (val result = courseDataValidator.validateArea(course.area)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    CourseDataValidator.AreaError.NO_SELECTED -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }
        }


        if (isValid.value){
            if (id != null){
//                updateCourseUseCase(course).onEach { result ->
//                    when(result){
//                        is Resource.Error -> {
//                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                            Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
//                            _stateCourse.value = AddCourseState(error = result.message)
//                        }
//                        is Resource.Loading -> {
//                            Timber.tag("AUTH_VM").e("is loading")
//                            _stateCourse.value = AddCourseState(isLoading = true)
//                        }
//                        is Resource.Success -> {
//                            Timber.tag("AUTH_VM").e("success")
//                            Log.e("AUTH_VM:", "success")
//                            _stateCourse.value = AddCourseState(info = result.data)
//                            Log.e("AUTH_VM:", "${stateCourse.value.info}")
//                            _stateCourse.value.info?.let {
//
//                                delay(1000)
//
//                            }
//                        }
//                    }
//
//                }.launchIn(viewModelScope)
            }else{
//                insertCourseUseCase(course).onEach { result ->
//                    when(result){
//                        is Resource.Error -> {
//                            //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                            Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
//                            _stateCourse.value = AddCourseState(error = result.message)
//                        }
//                        is Resource.Loading -> {
//                            Timber.tag("AUTH_VM").e("is loading")
//                            _stateCourse.value = AddCourseState(isLoading = true)
//                        }
//                        is Resource.Success -> {
//                            Timber.tag("AUTH_VM").e("success")
//                            Log.e("AUTH_VM:", "success")
//                            _stateCourse.value = AddCourseState(info = result.data)
//                            Log.e("AUTH_VM:", "${stateCourse.value.info}")
//                            _stateCourse.value.info?.let {
//
//                                delay(1000)
//
//                            }
//                        }
//                    }
//
//                }.launchIn(viewModelScope)
            }
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}