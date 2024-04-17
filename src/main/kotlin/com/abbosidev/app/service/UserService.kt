package com.abbosidev.app.service

import com.abbosidev.app.entity.User
import com.abbosidev.app.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun save(user: User): User = userRepository.save(user)

    fun findAll(): List<User> = userRepository.findAll()

}