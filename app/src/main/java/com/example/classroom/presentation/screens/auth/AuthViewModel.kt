package com.example.classroom.presentation.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.classroom.common.validator.UserDataValidator
import com.example.classroom.data.remote.dto.login.signIn.SignInRequestDto
import com.example.classroom.data.remote.dto.login.signUp.SignUpRequestDto
import com.example.classroom.presentation.screens.auth.signIn.SignInState
import com.example.classroom.presentation.screens.auth.signUp.SignUpState

class AuthViewModel(
    private val userDataValidator: UserDataValidator
): ViewModel() {

    private val _stateLoginUser = mutableStateOf(SignInState())
    val stateLoginUser: State<SignInState> = _stateLoginUser

    private val _stateRegisterUser = mutableStateOf(SignUpState())
    val stateRegisterUser: State<SignUpState> = _stateRegisterUser

    val nameField = mutableStateOf("")
    val lastnameField = mutableStateOf("")
    val phoneField = mutableStateOf("")
    val birthdateField = mutableStateOf("")
    val emailField = mutableStateOf("")
    val passwordField = mutableStateOf("")


    fun onSignInClick(user: SignInRequestDto) {
        val isValid = mutableStateOf(false)

        when (val result = userDataValidator.validateEmail(user.email)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.EmailError.REPEATED -> {
                        isValid.value = false
                    }
                    UserDataValidator.EmailError.INVALIDATE -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }


        when (val result = userDataValidator.validatePassword(user.password)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.PasswordError.TOO_SHORT -> {
                        isValid.value = false
                    }
                    UserDataValidator.PasswordError.NO_UPPERCASE -> {
                        isValid.value = false
                    }
                    UserDataValidator.PasswordError.NO_DIGIT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        if (isValid.value){

        }
    }

    fun onSignUpClick(user: SignUpRequestDto) {
        val isValid = mutableStateOf(false)
        when (val result = userDataValidator.validateName(user.name)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.NameError.HAS_DIGIT -> {
                        isValid.value = false
                    }
                    UserDataValidator.NameError.TOO_SHORT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        when (val result = userDataValidator.validateName(user.lastname)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.NameError.HAS_DIGIT -> {
                        isValid.value = false
                    }
                    UserDataValidator.NameError.TOO_SHORT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        when (val result = userDataValidator.validateEmail(user.email)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.EmailError.REPEATED -> {
                        isValid.value = false
                    }
                    UserDataValidator.EmailError.INVALIDATE -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        when (val result = userDataValidator.validateBirthdate(user.birthdate)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.BirthdateError.TOO_OLD -> {
                        isValid.value = false
                    }
                    UserDataValidator.BirthdateError.TOO_YOUNG -> {
                        isValid.value = false
                    }
                    UserDataValidator.BirthdateError.INVALIDATE -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        when (val result = userDataValidator.validatePhone(user.phone)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.PhoneError.REPEATED -> {
                        isValid.value = false
                    }
                    UserDataValidator.PhoneError.INVALIDATE -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        when (val result = userDataValidator.validatePassword(user.password)) {
            is com.example.classroom.common.validator.Result.Error -> {
                when (result.error) {
                    UserDataValidator.PasswordError.TOO_SHORT -> {
                        isValid.value = false
                    }
                    UserDataValidator.PasswordError.NO_UPPERCASE -> {
                        isValid.value = false
                    }
                    UserDataValidator.PasswordError.NO_DIGIT -> {
                        isValid.value = false
                    }
                }
            }

            is com.example.classroom.common.validator.Result.Success -> {
                isValid.value = true
            }

        }

        if (isValid.value){

        }
    }
}