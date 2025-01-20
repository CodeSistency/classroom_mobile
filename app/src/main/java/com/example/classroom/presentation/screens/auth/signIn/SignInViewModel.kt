package com.example.classroom.presentation.screens.auth.signIn

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.use_case.signIn.SignInUseCase
import com.example.classroom.presentation.screens.auth.signIn.states.SignInState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber


class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val loginRepositoryImp: LoginRepositoryImpl
) : ViewModel() {

    private val _stateLoginUser = mutableStateOf(SignInState())
    val stateLoginUser: State<SignInState> = _stateLoginUser

    var email = mutableStateOf("")
    var password = mutableStateOf("")

    // Validation states
    var emailError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)

    // Check if form is valid

    fun cleanInfo(){
        _stateLoginUser.value = _stateLoginUser.value.copy(isLoading = false, info = null, error = null)
    }
    val isFormValid: Boolean
        get() = emailError.value == null &&
                passwordError.value == null &&
                email.value.isNotBlank() &&
                password.value.isNotBlank()

    // Validation Logic
    fun validateEmail() {
        emailError.value = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            "El correo electrónico no es válido"
        } else null
    }

    fun validatePassword() {
        passwordError.value = if (password.value.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
    }

    // Sign-in execution
    suspend fun executeSignIn() {
        val user = SignInRequestDto(
            email = email.value,
            password = password.value
        )

        signInUseCase(user).onEach { result ->
            when(result) {
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
                    _stateLoginUser.value = SignInState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("AUTH_VM").e("is loading")
                    _stateLoginUser.value = SignInState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("AUTH_VM").e("success")
                    Log.e("AUTH_VM:", "success")
                    _stateLoginUser.value = SignInState(info = result.data)
                    Log.e("AUTH_VM:", "${stateLoginUser.value.info}")
                    _stateLoginUser.value.info?.let {
                        insertUserDb(it)
                        delay(300)

                    }
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    suspend fun insertUserDb(user: LocalUser){
        loginRepositoryImp.insertLocalUser(user)
    }
}