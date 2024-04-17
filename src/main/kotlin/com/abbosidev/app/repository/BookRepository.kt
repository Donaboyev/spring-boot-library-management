package com.abbosidev.app.repository

import com.abbosidev.app.entity.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, Long>