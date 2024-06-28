package se.hse.kpo.homework.three.entity

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import java.sql.Timestamp
import java.util.Date

@Entity
@Table (name = "user", schema = "public")
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val nickname: String,

    val email: String,

    val password: String,

    val created: Timestamp

) {constructor() : this(1, "admin", "admin@mail.com", "admin", Timestamp(Date().time))}
