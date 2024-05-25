package com.example.classroom.presentation.screens.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.data.repository.LoginRepositoryImpl
import com.example.classroom.domain.model.entity.Gender
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.domain.use_case.signIn.SignInUseCase
import com.example.classroom.domain.use_case.signUp.SignUpUseCase
import com.example.classroom.domain.use_case.validators.signIn.SignInValidator
import com.example.classroom.domain.use_case.validators.signUp.SignUpValidator
import com.example.classroom.presentation.navigation.Destination
import com.example.classroom.presentation.screens.auth.signIn.SignInFormEvent
import com.example.classroom.presentation.screens.auth.signIn.states.SignInFormState
import com.example.classroom.presentation.screens.auth.signIn.states.SignInState
import com.example.classroom.presentation.screens.auth.signUp.SignUpFormEvent
import com.example.classroom.presentation.screens.auth.signUp.states.SignUpFormState
import com.example.classroom.presentation.screens.auth.signUp.states.SignUpState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.common.Resource
import timber.log.Timber

class AuthViewModel(
    private val userDataValidator: UserDataValidator,
    private val signUpValidator: SignUpValidator,
    private val signInValidator: SignInValidator,
    private val signUpUseCase: SignUpUseCase,
    private val signInUseCase: SignInUseCase,
    private val loginRepositoryImp: LoginRepositoryImpl
): ViewModel() {

    private val _stateLoginUser = mutableStateOf(SignInState())
    val stateLoginUser: State<SignInState> = _stateLoginUser

    var stateLoginForm by mutableStateOf(SignInFormState())

    private val _stateRegisterUser = mutableStateOf(SignUpState())
    val stateRegisterUser: State<SignUpState> = _stateRegisterUser

    var stateRegisterForm by mutableStateOf(SignUpFormState())

    private val validationEventRegisterChannel = Channel<ValidationEvent>()
    val validationRegisterEvents = validationEventRegisterChannel.receiveAsFlow()

    private val validationEventLoginChannel = Channel<ValidationEvent>()
    val validationLoginEvents = validationEventLoginChannel.receiveAsFlow()

    fun onSignUpEvent(event: SignUpFormEvent) {
        when(event) {
            is SignUpFormEvent.EmailChanged -> {
                stateRegisterForm = stateRegisterForm.copy(email = event.email)
            }
            is SignUpFormEvent.PasswordChanged -> {
                stateRegisterForm = stateRegisterForm.copy(password = event.password)
            }
            is SignUpFormEvent.GenderChanged -> {
                stateRegisterForm = stateRegisterForm.copy(gender = event.gender)
            }
            is SignUpFormEvent.LastnameChanged -> {
                stateRegisterForm = stateRegisterForm.copy(lastname = event.lastname)
            }
            is SignUpFormEvent.NameChanged ->{
                stateRegisterForm = stateRegisterForm.copy(name = event.name)
            }
            is SignUpFormEvent.PhoneChanged -> {
                stateRegisterForm = stateRegisterForm.copy(phone = event.phone)
            }
            SignUpFormEvent.Submit -> submitSignUpData()
        }
    }


    private fun submitSignUpData() {
        val emailResult = signUpValidator.validateEmail.execute(stateRegisterForm.email)
        val passwordResult = signUpValidator.validatePassword.execute(stateRegisterForm.password)
        val nameResult = signUpValidator.validateNames.execute(stateRegisterForm.name)
        val lastnameResult = signUpValidator.validateNames.execute(stateRegisterForm.lastname)
        val phoneResult = signUpValidator.validatePhone.execute(stateRegisterForm.phone)



        val hasError = listOf(
            emailResult,
            passwordResult,
            nameResult,
            lastnameResult,
            phoneResult
        ).any { !it.successful }

        if(hasError) {
            stateRegisterForm = stateRegisterForm.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                nameError = nameResult.errorMessage,
                lastnameError = lastnameResult.errorMessage,
                phoneError = phoneResult.errorMessage,
            )
            return
        }
        Log.e("final", "final")
        viewModelScope.launch {
            Log.e("final2", "final2")
            executeSignUp()
            validationEventRegisterChannel.send(ValidationEvent.Success)
        }
    }

     suspend fun executeSignUp(){
        Log.e("final3", "final3")

        val user = SignUpRequestDto(
            password = stateRegisterForm.password,
            email = stateRegisterForm.email,
            name = stateRegisterForm.name,
            phone = stateRegisterForm.phone,
            lastname = stateRegisterForm.lastname,
            gender = stateRegisterForm.gender,
            birthdate = stateRegisterForm.birthdate
        )
        Log.e("user", "$user")
        signUpUseCase(user).onEach { result ->
            when(result){
                is Resource.Error -> {
                    //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
                    Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
                    _stateRegisterUser.value = SignUpState(error = result.message)
                }
                is Resource.Loading -> {
                    Timber.tag("AUTH_VM").e("is loading")
                    _stateRegisterUser.value = SignUpState(isLoading = true)
                }
                is Resource.Success -> {
                    Timber.tag("AUTH_VM").e("success")
                    Log.e("AUTH_VM:", "success")
                    _stateRegisterUser.value = SignUpState(info = result.data)
                    Log.e("AUTH_VM:", "${_stateRegisterUser.value.info}")

                }
                else -> {}
            }
        }.launchIn(viewModelScope)

    }

    fun onSignInEvent(event: SignInFormEvent) {
        when(event) {
            is SignInFormEvent.EmailChanged -> {
                stateLoginForm = stateLoginForm.copy(email = event.email)
            }
            is SignInFormEvent.PasswordChanged -> {
                stateLoginForm = stateLoginForm.copy(password = event.password)
            }
            SignInFormEvent.Submit -> submitSignInData()
        }
    }

    private fun submitSignInData() {
        val emailResult = signInValidator.validateEmail.execute(stateLoginForm.email)
        val passwordResult = signInValidator.validatePassword.execute(stateLoginForm.password)

        val hasError = listOf(
            emailResult,
            passwordResult,
        ).any { !it.successful }

        if(hasError) {
            stateLoginForm = stateLoginForm.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
            )
            return
        }
        viewModelScope.launch {
            executeSignIn()
            validationEventLoginChannel.send(ValidationEvent.Success)

        }
    }

    suspend fun executeSignIn(){
        val user = SignInRequestDto(
            password = stateLoginForm.password,
            email = stateLoginForm.email,

            )
        signInUseCase(user).onEach { result ->
            when(result){
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
            }
        }.launchIn(viewModelScope)

    }
//    fun onInputRegisterChange(field: String, newValue: String) {
//        _stateRegisterForm.value = when (field) {
//            "name" -> _stateRegisterForm.value.copy(name = newValue)
//            "lastname" -> _stateRegisterForm.value.copy(lastname = newValue)
//            "email" -> _stateRegisterForm.value.copy(email = newValue)
//            "birthdate" -> _stateRegisterForm.value.copy(birthdate = newValue)
//            "phone" -> _stateRegisterForm.value.copy(phone = newValue)
//            "password" -> _stateRegisterForm.value.copy(password = newValue)
//            else -> _stateRegisterForm.value
//        }
//    }

//    suspend fun onSignInClick(user: SignInRequestDto, navController: NavController) {
//        val isValid = mutableStateOf(false)
//        isValid.value = false
//
//        when (val result = userDataValidator.validateEmail(user.email)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.EmailError.REPEATED -> {
//                        stateLoginForm.value.emailError = "Email está repetido"
//                        isValid.value = false
//                    }
//                    UserDataValidator.EmailError.INVALIDATE -> {
//                        stateLoginForm.value.emailError = "Email es invalido"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateLoginForm.value.emailError = null
//                isValid.value = true
//            }
//
//        }
//
//
//        when (val result = userDataValidator.validatePassword(user.password)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.PasswordError.TOO_SHORT -> {
//                        stateLoginForm.value.passwordError = "Contraseña es muy corta"
//                        isValid.value = false
//                    }
//                    UserDataValidator.PasswordError.NO_UPPERCASE -> {
//                        stateLoginForm.value.passwordError = "Contraseña no tiene mayusculas"
//                        isValid.value = false
//                    }
//                    UserDataValidator.PasswordError.NO_DIGIT -> {
//                        stateLoginForm.value.passwordError = "Contraseña no tienes caracter numerico"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateLoginForm.value.passwordError = null
//                isValid.value = true
//            }
//
//        }
//
//        if (isValid.value){
//            signInUseCase(user).onEach { result ->
//                when(result){
//                    is Resource.Error -> {
//                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                        Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
//                        _stateLoginUser.value = SignInState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        Timber.tag("AUTH_VM").e("is loading")
//                        _stateLoginUser.value = SignInState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        Timber.tag("AUTH_VM").e("success")
//                        Log.e("AUTH_VM:", "success")
//                        _stateLoginUser.value = SignInState(info = result.data)
//                        Log.e("AUTH_VM:", "${stateLoginUser.value.info}")
//                        _stateLoginUser.value.info?.let {
//                            insertUserDb(it)
//                            delay(300)
//                            navController.navigate(Destination.HOME.screenRoute)
//
//                        }
//                    }
//                }
//            }
//        }
//    }

//    suspend fun onSignUpClick(user: SignUpRequestDto, navController: NavController) {
//        val isValid = mutableStateOf(false)
//        isValid.value = false
//        when (val result = userDataValidator.validateName(user.name)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.NameError.HAS_DIGIT -> {
//                        stateRegisterForm.value.nameError = "El nombre tiene un numero"
//                        isValid.value = false
//                    }
//                    UserDataValidator.NameError.TOO_SHORT -> {
//                        stateRegisterForm.value.nameError = "El nombre es muy corto"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateRegisterForm.value.nameError = null
//                isValid.value = true
//            }
//
//        }
//
//        when (val result = userDataValidator.validateName(user.lastname)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.NameError.HAS_DIGIT -> {
//                        stateRegisterForm.value.lastnameError = "El apellido tiene numero"
//                        isValid.value = false
//                    }
//                    UserDataValidator.NameError.TOO_SHORT -> {
//                        stateRegisterForm.value.lastnameError = "El apellido es muy corto"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateRegisterForm.value.lastnameError = null
//                isValid.value = true
//            }
//
//        }
//
//        when (val result = userDataValidator.validateEmail(user.email)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.EmailError.REPEATED -> {
//                        stateRegisterForm.value.emailError = "El email esta repetido"
//                        isValid.value = false
//                    }
//                    UserDataValidator.EmailError.INVALIDATE -> {
//                        stateRegisterForm.value.emailError = "El email es invalido"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateRegisterForm.value.emailError = null
//                isValid.value = true
//            }
//
//        }
//
//        when (val result = userDataValidator.validateBirthdate(user.birthdate)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.BirthdateError.TOO_OLD -> {
//                        stateRegisterForm.value.birthdateError = "La persona excede los limites de edad"
//                        isValid.value = false
//                    }
//                    UserDataValidator.BirthdateError.TOO_YOUNG -> {
//                        stateRegisterForm.value.birthdateError = "La persona es muy joven"
//                        isValid.value = false
//                    }
//                    UserDataValidator.BirthdateError.INVALIDATE -> {
//                        stateRegisterForm.value.birthdateError = "La fecha es invalida"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateRegisterForm.value.birthdateError = null
//                isValid.value = true
//            }
//
//        }
//
//        when (val result = userDataValidator.validatePhone(user.phone)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.PhoneError.REPEATED -> {
//                        stateRegisterForm.value.phoneError = "El telefono esta repetido"
//                        isValid.value = false
//                    }
//                    UserDataValidator.PhoneError.INVALIDATE -> {
//                        stateRegisterForm.value.phoneError = "El telefono es ivalido"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateRegisterForm.value.phoneError = null
//                isValid.value = true
//            }
//
//        }
//
//        when (val result = userDataValidator.validatePassword(user.password)) {
//            is com.example.classroom.common.validator.Result.Error -> {
//                when (result.error) {
//                    UserDataValidator.PasswordError.TOO_SHORT -> {
//                        stateRegisterForm.value.passwordError = "Contraseña es muy corta"
//                        isValid.value = false
//                    }
//                    UserDataValidator.PasswordError.NO_UPPERCASE -> {
//                        stateRegisterForm.value.passwordError = "Contraseña no tiene mayusculas"
//                        isValid.value = false
//                    }
//                    UserDataValidator.PasswordError.NO_DIGIT -> {
//                        stateRegisterForm.value.passwordError = "Contraseña no tienes caracter numerico"
//                        isValid.value = false
//                    }
//                }
//            }
//
//            is com.example.classroom.common.validator.Result.Success -> {
//                stateRegisterForm.value.passwordError = null
//                isValid.value = true
//            }
//
//        }
//
//        if (isValid.value){
//            signUpUseCase(user).onEach { result ->
//                when(result){
//                    is Resource.Error -> {
//                        //Timber.tag("AUTH_VM").e("Error ${result.message?.uiMessage}")
//                        Log.e("AUTH_VM:", "Error ${result.message?.uiMessage}")
//                        _stateRegisterUser.value = SignUpState(error = result.message)
//                    }
//                    is Resource.Loading -> {
//                        Timber.tag("AUTH_VM").e("is loading")
//                        _stateRegisterUser.value = SignUpState(isLoading = true)
//                    }
//                    is Resource.Success -> {
//                        Timber.tag("AUTH_VM").e("success")
//                        Log.e("AUTH_VM:", "success")
//                        _stateRegisterUser.value = SignUpState(isLoading = false)
//                        Log.e("AUTH_VM:", "${stateLoginUser.value.info}")
//                        _stateRegisterUser.value.info?.let {
//                            _stateRegisterUser.value = SignUpState(isLoading = false, info = "Success")
//
//                        }
//                    }
//                    else -> {}
//                }
//            }.launchIn(viewModelScope)
//        }
//    }

    suspend fun insertUserDb(user: LocalUser){
        loginRepositoryImp.insertLocalUser(user)
    }
    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}