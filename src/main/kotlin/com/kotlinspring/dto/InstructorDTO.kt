package com.kotlinspring.dto

import com.kotlinspring.entity.Course
import javax.persistence.*
import javax.validation.constraints.NotBlank

data class InstructorDTO(
    val id: Int?,
    @get:NotBlank(message = "instrutorDTO.name must not be blank")
    val name: String,
)
