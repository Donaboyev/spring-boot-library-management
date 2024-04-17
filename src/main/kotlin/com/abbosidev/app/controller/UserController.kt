package com.abbosidev.app.controller

import com.abbosidev.app.entity.User
import com.abbosidev.app.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun addUser(@RequestBody user: User) = userService.save(user)

    @GetMapping
    fun getUsers() = userService.findAll()

}