package com.abbosidev.app.service

import com.abbosidev.app.entity.Book
import com.abbosidev.app.repository.BookRepository
import com.abbosidev.app.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository
) {

    fun findAll(): List<Book> = bookRepository.findAll()

    fun findById(id: Long): Book? = bookRepository.findByIdOrNull(id)

    fun save(book: Book): Book = bookRepository.save(book)

    fun deleteById(id: Long) = bookRepository.deleteById(id)

    fun borrowBook(bookId: Long, userId: Long): Book {
        val user = userRepository.findById(userId)
            .orElseThrow { throw RuntimeException("User with ID $userId not found") }

        val book = bookRepository
            .findById(bookId)
            .orElseThrow { throw RuntimeException("Book with ID $bookId not found") }

        if (book.borrowed) {
            throw RuntimeException("Book with ID $bookId already borrowed")
        }
        return bookRepository.save(
            book.apply {
                borrowed = true
                borrowedBy = user
            }
        )
    }

    fun returnBook(bookId: Long): Book {
        val book = bookRepository.findById(bookId)
            .orElseThrow { throw RuntimeException("Book with ID $bookId not found") }
        if (book.borrowed) {
            return bookRepository.save(
                book.apply {
                    borrowed = false
                    borrowedBy = null
                }
            )
        }
        return book
    }
}