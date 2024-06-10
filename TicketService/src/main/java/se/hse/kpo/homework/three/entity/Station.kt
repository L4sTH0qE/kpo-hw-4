package se.hse.kpo.homework.three.entity

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import java.sql.Timestamp
import java.util.Date

@Entity
@Table (name = "station", schema = "public")
data class Station (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val station: String

) {constructor() : this(1, "Moscow")}
