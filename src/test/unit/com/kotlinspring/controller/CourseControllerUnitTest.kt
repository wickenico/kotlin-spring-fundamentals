package com.kotlinspring.controller;

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.kotlinspring.unit.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(null, "Restful API", category = "Nico Wickersheim")

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }

    @Test
    fun retrieveAllCourses() {

        every { courseServiceMockk.retrieveAllCourses() }.returnsMany(
            listOf(courseDTO(id = 1), courseDTO(id = 2, name = "Build RestFul APis using SpringBoot and Kotlin")
        ))

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, courseDTOs!!.size)

    }

    @Test
    fun updateCourse() {

        val updatedCourseDTO = CourseDTO(null,
            "Build RestFul APis using SpringBoot and Kotlin1", "Development")

        every { courseServiceMockk.updateCourse(any(), any()) } returns courseDTO(id=100,
            name = "Build RestFul APis using SpringBoot and Kotlin1")
                //CourseDTO(100,
            //"Build RestFul APis using SpringBoot and Kotlin1", "Development")

        val updateCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build RestFul APis using SpringBoot and Kotlin1", updateCourse!!.name)

    }

    @Test
    fun deleteCourse() {

        every { courseServiceMockk.deleteCourse(any()) } just runs

        val updateCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent

    }

    @Test
    fun addCourse_validation() {

        val courseDTO = CourseDTO(null, "", "")

        every { courseServiceMockk.addCourse(any()) } returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("courseDTo.category must not be blank, courseDTo.name must not be blank", response)
    }

    @Test
    fun addCourse_runtimeException() {

        val courseDTO = CourseDTO(null, "Restful API", category = "Nico Wickersheim")

        val errorMessage = "Unexpected Error occurred"
        every { courseServiceMockk.addCourse(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, response)
    }

}
