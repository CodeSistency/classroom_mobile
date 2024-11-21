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
import com.example.classroom.domain.model.entity.Role
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
            genderId = 1,
            birthdate = stateRegisterForm.birthdate,
            roleId = 0,
            username = ""
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

    suspend fun executeSignUpNew(){
        Log.e("final3", "final3")

        val user = SignUpRequestDto(
            password = password.value,
            email = email.value,
            name = name.value,
            username = username.value,
            phone = phone.value,
            lastname = lastname.value,
            genderId = gender.value.id,
            birthdate = birthdate.value,
            roleId = role.value.id,
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
            else -> {}
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

    //SIGN UP FORM(VALIDATION)

    var name = mutableStateOf("")
    var lastname = mutableStateOf("")
    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var email = mutableStateOf("")
    var birthdate = mutableStateOf("")
    var phone = mutableStateOf("")
    var gender = mutableStateOf(Gender.Man)
    var role = mutableStateOf(Role.STUDENT) // Default role, e.g., "Student"


    // Validation States
    var nameError = mutableStateOf<String?>(null)
    var lastnameError = mutableStateOf<String?>(null)
    var usernameError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)
    var emailError = mutableStateOf<String?>(null)
    var birthdateError = mutableStateOf<String?>(null)
    var phoneError = mutableStateOf<String?>(null)

    // Check if form is valid
    val isFormValid: Boolean
        get() = nameError.value == null &&
                lastnameError.value == null &&
                usernameError.value == null &&
                passwordError.value == null &&
                emailError.value == null &&
                birthdateError.value == null &&
                phoneError.value == null &&
                name.value.isNotBlank() &&
                lastname.value.isNotBlank() &&
                username.value.isNotBlank() &&
                password.value.isNotBlank() &&
                email.value.isNotBlank() &&
                birthdate.value.isNotBlank() &&
                phone.value.isNotBlank()

    // Validation Logic
    fun validateName() {
        nameError.value = if (name.value.isBlank()) "El nombre es obligatorio" else null
    }

    fun validateLastname() {
        lastnameError.value = if (lastname.value.isBlank()) "El apellido es obligatorio" else null
    }
    fun validateUsername() {
        usernameError.value = if (username.value.isBlank()) "username es obligatorio" else null
    }
    fun validatePassword() {
        passwordError.value = if (password.value.length < 6) "La contraseña debe tener al menos 6 caracteres" else null
    }

    fun validateEmail() {
        emailError.value = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            "El correo electrónico no es válido"
        } else null
    }

    fun validateBirthdate() {
        birthdateError.value = if (birthdate.value.isBlank()) "La fecha de nacimiento es obligatoria" else null
    }

    fun validatePhone() {
        phoneError.value = if (!phone.value.matches(Regex("^\\+?[0-9]{10,13}\$"))) {
            "El teléfono no es válido"
        } else null
    }

    suspend fun insertUserDb(user: LocalUser){
        loginRepositoryImp.insertLocalUser(user)
    }
    sealed class ValidationEvent {
        object Success: ValidationEvent()
    }
}