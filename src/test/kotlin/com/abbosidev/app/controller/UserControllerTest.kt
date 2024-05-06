package com.abbosidev.app.controller

import com.abbosidev.app.repository.UserRepository
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Order(1)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    private val tempUserName = "testUserName"

    companion object {
        var userId = -1L
    }

    @Order(1)
    @Test
    fun addUser() {
        mockMvc
            .perform(
                post("/api/users")
                    .contentType(APPLICATION_JSON)
                    .content("{\"name\":\"$tempUserName\"}")
            )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(tempUserName))
            .andExpect(jsonPath("$.id").isNumber)
            .andDo { result ->
                val response = result.response.contentAsString
                userId = JsonPath.parse(response).read<Long?>("id").toLong()
            }
    }

    @Order(2)
    @Test
    fun checkWhetherUserSaved() {
        val user = userRepository.findById(userId)
        assertThat(user.isPresent).isTrue()
        assertEquals(tempUserName, user.get().name)
    }

    @Order(3)
    @Test
    fun getAllUsers() {
        mockMvc
            .perform(
                get("/api/users")
                    .contentType(APPLICATION_JSON)
            )
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andDo { result ->
                val response = result.response.contentAsString
                val list = JsonPath.parse(response).read<List<Any>>("$")
                assertThat(list).hasSize(1)
                assertEquals(userId.toInt(), (list[0] as Map<*, *>)["id"])
                assertEquals(tempUserName, (list[0] as Map<*, *>)["name"])
            }
    }
}