package com.abbosidev.app.controller

import com.abbosidev.app.entity.Book
import com.abbosidev.app.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/books")
class BookController(private val bookService: BookService) {

    @GetMapping
    fun getAllBooks() = bookService.findAll()

    @GetMapping("/{id}")
    fun getBook(@PathVariable("id") id: Long): ResponseEntity<Book> {
        val book = bookService.findById(id)
        return if (book != null) {
            ResponseEntity.ok(book)
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping
    fun addBook(@RequestBody book: Book) = bookService.save(book)

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable("id") id: Long): ResponseEntity<Void> {
        bookService.findById(id) ?: return ResponseEntity.badRequest().build()
        bookService.deleteById(id)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{bookId}/borrow/{userId}")
    fun borrowBook(@PathVariable bookId: Long, @PathVariable userId: Long) = bookService.borrowBook(bookId, userId)

    @PostMapping("/{bookId}/return")
    fun returnBook(@PathVariable bookId: Long) = bookService.returnBook(bookId)

}