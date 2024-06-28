package se.hse.kpo.homework.three.service

import org.springframework.data.jpa.repository.Query
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import se.hse.kpo.homework.three.entity.Order
import se.hse.kpo.homework.three.repository.OrderRepository
import java.util.Random

@Service
open class OrderProcessorEmulator(private val orderRepository: OrderRepository) {

        // Execute every 15 seconds
        @Scheduled(fixedRate = 5000)
        open fun processOrders() {
            val checkOrders: List<Order>? = orderRepository.findAllByStatus(1)
            val random: Random = Random()
            if (!checkOrders.isNullOrEmpty()) {
                val ind: Long = checkOrders[random.nextInt(checkOrders.size)].id ?: 0
                if (random.nextBoolean()) {
                    orderRepository.updateStatusById(2, ind)    // Set 'Success' status
                } else {
                    orderRepository.updateStatusById(3, ind)   // Set 'Rejection' status
                }
            }
        }
}