package se.hse.kpo.homework.three.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.criteria.CriteriaBuilder.In
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import se.hse.kpo.homework.three.dto.LoginDTO
import se.hse.kpo.homework.three.dto.RegisterDTO
import se.hse.kpo.homework.three.entity.Session
import se.hse.kpo.homework.three.entity.User
import se.hse.kpo.homework.three.repository.SessionRepository
import se.hse.kpo.homework.three.repository.UserRepository
import java.sql.Timestamp
import java.util.*
import kotlin.jvm.optionals.getOrNull


@Service
class AuthService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder,
                  private val jwtService: JwtService, private val authenticationManager: AuthenticationManager,
                  private val sessionRepository: SessionRepository
) {

    fun register(request: RegisterDTO): String {
        if (userRepository.findByEmail(request.email ?: "") != null) {
            throw Exception("User with such email already exists")
        }
        val user = User(null, request.nickname ?: "", request.email ?: "", passwordEncoder.encode(request.password ?: ""), Timestamp(Date().time))
        userRepository.save(user)

        return "User registered successfully"
    }

    fun authenticate(request: LoginDTO): String {
        try {
            val authentication =
                authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.email ?: "", request.password ?: ""))
            val token = jwtService.generateToken(authentication)
            val user = userRepository.findByEmail(request.email ?: "") ?: throw Exception("No user with such email")
            val session = Session(null, user.id ?: 1, token, Timestamp(Date().time + 24 * 60 * 60 * 1000))

            sessionRepository.save(session)

            return token;
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun getUserByToken(token: String): String {
        try {
            // Get the session by the JWT token
            val session = sessionRepository.findByToken(token) ?: throw Exception("Invalid token")

            // Validate and parse the JWT token

            if (session.expires < Timestamp(Date().time)) {
                throw Exception("Token expired")
            }

            // Get the user by id
            val user = userRepository.findById(session.userId).getOrNull() ?: throw Exception("Invalid token")
            return "id: " + user.id + "\nnickname: " + user.nickname + "\nemail:" + user.email + "\ncreated:" + user.created
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun getIdByToken(token: String): Long {
        try {
            // Get the session by the JWT token
            val session = sessionRepository.findByToken(token) ?: throw Exception("Invalid token")

            // Validate and parse the JWT token

            if (session.expires < Timestamp(Date().time)) {
                throw Exception("Token expired")
            }

            return session.userId
        } catch (ex: Exception) {
            throw ex
        }
    }
}