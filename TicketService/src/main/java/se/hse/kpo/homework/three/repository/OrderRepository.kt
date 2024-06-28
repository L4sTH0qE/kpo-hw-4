package se.hse.kpo.homework.three.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import se.hse.kpo.homework.three.entity.Order
import org.springframework.transaction.annotation.Transactional;


@Repository
interface OrderRepository: JpaRepository<Order, Long> {
    fun findAllByStatus(status: Long): List<Order>?

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :newValue WHERE o.id = :entityId")
    fun updateStatusById(@Param("newValue") newValue: Long, @Param("entityId") entityId: Long)
}