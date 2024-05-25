package com.example.classroom.domain.use_case.validators

import com.example.classroom.domain.use_case.validators.activities.ActivitiesValidator
import com.example.classroom.domain.use_case.validators.courses.CoursesValidator
import com.example.classroom.domain.use_case.validators.signIn.SignInValidator
import com.example.classroom.domain.use_case.validators.signUp.SignUpValidator

data class ValidatorBundle(
    val signUpValidator: SignUpValidator,
    val signInValidator: SignInValidator,
    val coursesValidator: CoursesValidator,
    val activitiesValidator: ActivitiesValidator
)
