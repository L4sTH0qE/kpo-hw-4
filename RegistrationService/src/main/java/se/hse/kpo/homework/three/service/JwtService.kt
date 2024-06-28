package se.hse.kpo.homework.three.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    private val secretKey = "my0character0ultra0secure0and0ultra0loooong0amogus"

    fun extractAllClaims(token: String): Claims {
        return Jwts.parser().verifyWith(signingKey())
            .build().parseSignedClaims(token).payload
    }

    fun isValid(token: String): Boolean {

        return !extractAllClaims(token).expiration.before(Date())
    }

    fun generateToken(authentication: Authentication): String {
        val user = authentication.principal as UserDetails;
        return Jwts.builder().subject(user.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
            .signWith(signingKey())
            .compact()
    }

    private fun signingKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}