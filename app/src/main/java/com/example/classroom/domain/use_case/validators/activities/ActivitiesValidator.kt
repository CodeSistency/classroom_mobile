package com.example.classroom.domain.use_case.validators.activities

import com.example.classroom.domain.use_case.validators.cases.ValidateDate
import com.example.classroom.domain.use_case.validators.cases.ValidateGrade
import com.example.classroom.domain.use_case.validators.cases.ValidateNames

data class ActivitiesValidator(
   val validateNames: ValidateNames,
   val validateDate: ValidateDate,
   val validateGrade: ValidateGrade
   )