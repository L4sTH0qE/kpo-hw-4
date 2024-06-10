package se.hse.kpo.homework.three.entity

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.Date

@Entity
@Table(name = "session", schema = "public")
data class Session(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val userId: Long,

    val token: String,

    val expires: Timestamp,

) {constructor() : this(1, 1, "token", Timestamp(Date().time))}