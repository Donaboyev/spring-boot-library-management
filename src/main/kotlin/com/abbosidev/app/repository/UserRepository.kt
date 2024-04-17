package com.abbosidev.app.repository

import com.abbosidev.app.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>