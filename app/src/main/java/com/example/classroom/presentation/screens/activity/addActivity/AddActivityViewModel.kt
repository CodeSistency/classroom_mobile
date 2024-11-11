package com.example.classroom.presentation.screens.activity.addActivity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.remote.dto.activities.ActivityRequestDto
import com.example.classroom.domain.model.entity.LocalActivities
import com.example.classroom.domain.model.entity.Status
import com.example.classroom.domain.use_case.activities.InsertActivityUseCase
import com.example.classroom.domain.use_case.activities.UpdateActivityUseCase
import com.example.classroom.presentation.screens.activity.addActivity.states.AddActivityState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class AddActivityViewModel(
    private val updateActivityUseCase: UpdateActivityUseCase,
    private val insertActivityUseCase: InsertActivityUseCase,
): ViewModel() {

    private val _stateAddActivity = mutableStateOf(AddActivityState())
    val stateAddActivity: State<AddActivityState> = _stateAddActivity

    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var grade = mutableStateOf(0)
    var email = mutableStateOf("")
    var startDate = mutableStateOf("")
    var endDate = mutableStateOf("")
    var status = mutableStateOf(Status.OPEN)

    // Estados de validación
    var titleError = mutableStateOf<String?>(null)
    var gradeError = mutableStateOf<String?>(null)
    var emailError = mutableStateOf<String?>(null)
    var startDateError = mutableStateOf<String?>(null)
    var endDateError = mutableStateOf<String?>(null)
    var statusError = mutableStateOf<String?>(null)

    // Validación general del formulario
    val isFormValid: Boolean
        get() = titleError.value == null &&
                gradeError.value == null &&
                emailError.value == null &&
                startDateError.value == null &&
                endDateError.value == null &&
                statusError.value == null &&
                title.value.isNotBlank() &&
                grade.value >= 0 &&
                email.value.isNotBlank() &&
                startDate.value.isNotBlank() &&
                endDate.value.isNotBlank()

    // Lógica de validación
    fun validateTitle() {
        titleError.value = if (title.value.isBlank()) "El título es obligatorio" else null
    }

    fun validateGrade() {
        gradeError.value = if (grade.value < 0) "La calificación no puede ser negativa" else null
    }

    fun validateEmail() {
        emailError.value = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            "El correo electrónico no es válido"
        } else null
    }

    fun validateStartDate() {
        startDateError.value = if (startDate.value.isBlank()) "La fecha de inicio es obligatoria" else null
    }

    fun validateEndDate() {
        endDateError.value = if (endDate.value.isBlank()) "La fecha de finalización es obligatoria" else null
    }

    fun validateStatus() {
        statusError.value = if (status.value == Status.NO_SELECTED) "Debe seleccionar un estado" else null
    }

    fun fillForm(activity: LocalActivities) {
        title.value = activity.title
        description.value = activity.description ?: ""
        grade.value = activity.grade
        startDate.value = activity.startDate
        endDate.value = activity.endDate
        status.value = activity.status
    }
    fun resetForm() {
        title.value = ""
        description.value = ""
        grade.value = 0
        email.value = ""
        startDate.value = ""
        endDate.value = ""
        status.value = Status.NO_SELECTED
    }

    suspend fun executeActivityRequest(id: String?, idCourse:String){

        var activity= ActivityRequestDto(
            idCourse = idCourse,
            title = title.value,
            description = description.value.takeIf { it.isNotBlank() },
            grade = grade.value,
            email = email.value,
            startDate = startDate.value,
            endDate = endDate.value,
            statusId = status.value.id
        )

        if (id != null){
            updateActivityUseCase(activity, id).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                        _stateAddActivity.value = AddActivityState(error = result.message)
                    }
                    is Resource.Loading -> {
                        Timber.tag("ACTIVITIES").e("is loading")
                        _stateAddActivity.value = AddActivityState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Timber.tag("ACTIVITIES_VM").e("success")
                        Log.e("ACTIVITIES:", "success")
                        _stateAddActivity.value = AddActivityState(info = result.data)
                        Log.e("ACTIVITIES:", "${_stateAddActivity.value.info}")
                        _stateAddActivity.value.info?.let {
//                            insertUserDb(it)
                            delay(300)

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }else {
            insertActivityUseCase(activity).onEach { result ->
                when(result){
                    is Resource.Error -> {
                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                        Log.e("ACTIVITIES:", "Error ${result.message?.uiMessage}")
                        _stateAddActivity.value = AddActivityState(error = result.message)
                    }
                    is Resource.Loading -> {
                        Timber.tag("ACTIVITIES").e("is loading")
                        _stateAddActivity.value = AddActivityState(isLoading = true)
                    }
                    is Resource.Success -> {
                        Timber.tag("ACTIVITIES_VM").e("success")
                        Log.e("ACTIVITIES:", "success")
                        _stateAddActivity.value = AddActivityState(info = result.data)
                        Log.e("ACTIVITIES:", "${_stateAddActivity.value.info}")
                        _stateAddActivity.value.info?.let {
//                            insertUserDb(it)
                            delay(300)

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }

    }

}