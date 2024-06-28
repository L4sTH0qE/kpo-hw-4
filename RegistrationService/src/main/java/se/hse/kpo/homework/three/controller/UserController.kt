package se.hse.kpo.homework.three.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import se.hse.kpo.homework.three.dto.LoginDTO
import se.hse.kpo.homework.three.entity.User
import se.hse.kpo.homework.three.dto.RegisterDTO
import se.hse.kpo.homework.three.service.AuthService


@RestController
@RequestMapping("/api")
class UserController(val authService: AuthService) {


    @PostMapping("/register")
    fun register(@RequestBody registrationDTO: RegisterDTO): ResponseEntity<String> {

        // Проверка входных данных на корректность
        if (registrationDTO.nickname.isNullOrEmpty()
            || registrationDTO.email.isNullOrEmpty()
            || registrationDTO.password.isNullOrEmpty()
        ) {
            return ResponseEntity.badRequest().body("Invalid fields")
        }
        if (!isValidEmail(registrationDTO.email)) {
            return ResponseEntity.badRequest().body("Invalid email")
        }
        if (!isValidPassword(registrationDTO.password)) {
            return ResponseEntity.badRequest().body("Invalid password")
        }

        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registrationDTO))
        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(ex.message)
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDTO: LoginDTO): ResponseEntity<String> {
        if (loginDTO.email.isNullOrEmpty()
            || loginDTO.password.isNullOrEmpty()
        ) {
            return ResponseEntity.badRequest().body("Invalid fields")
        }
        return try {
            ResponseEntity.ok(authService.authenticate(loginDTO))
        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(ex.message)
        }
    }

    @GetMapping("/get")
    fun getInfo(@RequestHeader("Authorization") authHeader: String?): ResponseEntity<String> {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid header");
        }
        val token = authHeader.substring(7)

        return try {
            ResponseEntity.ok(authService.getUserByToken(token))
        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(ex.message)
        }
    }

    @GetMapping("/get/{token}")
    fun getId(@PathVariable("token") token: String): ResponseEntity<Long> {

        return try {
            ResponseEntity.ok(authService.getIdByToken(token))
        } catch (ex: Exception) {
            ResponseEntity.ok(-1)
        }
    }

    // Проверка корректности email
    private fun isValidEmail(email: String): Boolean {
        val pattern = "^[A-Za-z0-9+_.-]+@\\S+\\.\\S+\$".toRegex()
        return pattern.matches(email)
    }

    // Проверка корректности пароля
    private fun isValidPassword(password: String): Boolean {
        val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$".toRegex()
        return pattern.matches(password)
    }
}
