package com.abbosidev.app.controller

import com.abbosidev.app.repository.BookRepository
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
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

@Order(2)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var bookRepository: BookRepository

    private val tempBookTitle = "testBookTitle"
    private val tempBookAuthor = "testBookAuthor"
    private val dummyBookId = -1L
    private val dummyUserId = -1L

    companion object {
        private var bookId = -1L
    }

    @Order(1)
    @Test
    fun addBook() {
        mockMvc
            .perform(
                post("/api/books")
                    .content("{\"title\":\"$tempBookTitle\", \"author\":\"$tempBookAuthor\"}")
                    .contentType(APPLICATION_JSON)
            )
            .andExpect(status().isOk)
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value(tempBookTitle))
            .andExpect(jsonPath("$.author").value(tempBookAuthor))
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.borrowed").value(false))
            .andExpect(jsonPath("$.borrowedBy").value(null))
            .andDo { result ->
                val response = result.response.contentAsString
                bookId = JsonPath.parse(response).read("$.id")
            }
    }

    @Order(2)
    @Test
    fun `check book saved`() {
        val book = bookRepository.findById(bookId)
        assertNotNull(book)
        assertEquals(tempBookTitle, book.get().title)
        assertEquals(tempBookAuthor, book.get().author)
        assertFalse(book.get().borrowed)
        assertNull(book.get().borrowedBy)
    }

    @Order(3)
    @Test
    fun `should fail if try to get book with invalid id`() {
        mockMvc
            .perform(get("/api/books/$dummyBookId"))
            .andExpect(status().isBadRequest)
    }

    @Order(4)
    @Test
    fun getBook() {
        mockMvc
            .perform(get("/api/books/$bookId"))
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value(tempBookTitle))
            .andExpect(jsonPath("$.author").value(tempBookAuthor))
            .andExpect(jsonPath("$.borrowed").value(false))
            .andExpect(jsonPath("$.borrowedBy").value(null))
            .andExpect(jsonPath("$.id").value(bookId))
    }

    @Order(5)
    @Test
    fun `should fail if try to borrow book with invalid user id`() {
        mockMvc
            .perform(post("/api/books/$bookId/borrow/$dummyUserId"))
            .andExpect(status().isInternalServerError)
    }
}