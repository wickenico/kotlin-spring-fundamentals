package com.kotlinspring.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CourseDTO(

    val id: Int?,
    @get:NotBlank(message = "courseDTo.name must not be blank")
    val name: String,
    @get:NotBlank(message = "courseDTo.category must not be blank")
    val category: String,
    @get:NotNull(message = "courseDTO.instructorId must not be blank")
    val instructorId: Int? = null,

    )