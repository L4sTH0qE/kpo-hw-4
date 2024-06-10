package se.hse.kpo.homework.three.entity

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails
import java.sql.Timestamp
import java.util.Date

@Entity
@Table (name = "order", schema = "public")
data class Order (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val userId: Long,

    val fromStationId: Long,

    val toStationId: Long,

    var status: Long,

    val created: Timestamp

) {constructor() : this(1, 1, 1, 1, 3, Timestamp(Date().time))}
