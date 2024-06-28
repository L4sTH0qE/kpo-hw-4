package se.hse.kpo.homework.three.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import se.hse.kpo.homework.three.entity.Session

@Repository
interface SessionRepository: JpaRepository<Session, Long> {
    fun findByToken(token: String): Session?
}