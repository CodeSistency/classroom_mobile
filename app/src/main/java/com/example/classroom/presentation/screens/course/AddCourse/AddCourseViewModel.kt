package com.example.classroom.presentation.screens.course.AddCourse

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.remote.dto.courses.CourseRequestDto
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.Area
import com.example.classroom.domain.model.entity.LocalCourses
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.use_case.courses.InsertCourseUseCase
import com.example.classroom.domain.use_case.courses.UpdateCourseUseCase
import com.example.classroom.presentation.screens.course.AddCourse.states.AddCourseState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class AddCourseViewModel(
    private val insertCourseUseCase: InsertCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val repositoryBundle: RepositoryBundle,

    ): ViewModel() {

    private val _stateCourse = mutableStateOf(AddCourseState())
    val stateCourse: State<AddCourseState> = _stateCourse

    var userInfo: List<LocalUser>? = emptyList()

    init {
        viewModelScope.launch {
            userInfo = repositoryBundle.loginRepository.getUserInfo()

        }
    }

    var token = mutableStateOf("")
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var ownerId = mutableStateOf(0)
    var ownerName = mutableStateOf("")
    var section = mutableStateOf("")
    var subject = mutableStateOf("")
    var area = mutableStateOf(Area.NO_SELECTED)

    // Estados de validación
    var tokenError = mutableStateOf<String?>(null)
    var titleError = mutableStateOf<String?>(null)
    var ownerIdError = mutableStateOf<String?>(null)
    var ownerNameError = mutableStateOf<String?>(null)
    var sectionError = mutableStateOf<String?>(null)
    var subjectError = mutableStateOf<String?>(null)
    var areaError = mutableStateOf<String?>(null)

    // Validación general del formulario
    val isFormValid: Boolean
        get() = tokenError.value == null &&
                titleError.value == null &&
                ownerIdError.value == null &&
                ownerNameError.value == null &&
                sectionError.value == null &&
                subjectError.value == null &&
                token.value.isNotBlank() &&
                title.value.isNotBlank() &&
                ownerId.value > 0 &&
                ownerName.value.isNotBlank() &&
                section.value.isNotBlank() &&
                subject.value.isNotBlank() &&
                area.value != Area.NO_SELECTED

    // Lógica de validación
    fun validateToken() {
        tokenError.value = if (token.value.isBlank()) "El token es obligatorio" else null
    }

    fun validateTitle() {
        titleError.value = if (title.value.isBlank()) "El título es obligatorio" else null
    }

    fun validateOwnerId() {
        ownerIdError.value = if (ownerId.value <= 0) "El ID del propietario debe ser mayor que cero" else null
    }

    fun validateOwnerName() {
        ownerNameError.value = if (ownerName.value.isBlank()) "El nombre del propietario es obligatorio" else null
    }

    fun validateSection() {
        sectionError.value = if (section.value.isBlank()) "La sección es obligatoria" else null
    }

    fun validateSubject() {
        subjectError.value = if (subject.value.isBlank()) "La materia es obligatoria" else null
    }

    fun validateArea() {
        areaError.value = if (area.value == Area.NO_SELECTED) "Debe seleccionar un área" else null
    }

    fun fillForm(course: LocalCourses) {
        token.value = course.token
        title.value = course.title
        description.value = course.description ?: ""
        ownerName.value = course.ownerName
        section.value = course.section
        subject.value = course.subject
        area.value = course.area ?: Area.NO_SELECTED
    }

    fun resetForm() {
        token.value = ""
        title.value = ""
        description.value = ""
        ownerId.value = 0
        ownerName.value = ""
        section.value = ""
        subject.value = ""
        area.value = Area.NO_SELECTED
    }

    suspend fun executeCourseRequest(id: String?){

        var course = userInfo?.first()?.let {
            CourseRequestDto(
                description = description.value,
                title = title.value,
                token = token.value,
                ownerName = it.name,
                subject = subject.value,
                section = section.value,
                areaId = area.value.id,
                ownerId = it.idApi.toInt(),
            )
        }

        if (id != null){
            if (course != null) {
                updateCourseUseCase(course, id).onEach { result ->
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
                                repositoryBundle.coursesRepository.insertCourse(it)
        //                            insertUserDb(it)
                                delay(300)

                            }
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }else {
            if (course != null) {
                insertCourseUseCase(course).onEach { result ->
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

    }
}

