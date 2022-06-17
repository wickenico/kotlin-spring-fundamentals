package com.kotlinspring.controller

import com.kotlinspring.dto.InstructorDTO
import com.kotlinspring.service.InstructorService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMockk: InstructorService

    @Test
    fun addInstructor() {
        //given
        val instructorDTO = InstructorDTO(null, "Dilip Sundarraj")

        every { instructorServiceMockk.addNewInstructor(any()) } returns InstructorDTO(1, "Dilip Sundarraj")

        val savedInstructorDTO = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        //then
        Assertions.assertTrue {
            savedInstructorDTO!!.id != null
        }
    }

}