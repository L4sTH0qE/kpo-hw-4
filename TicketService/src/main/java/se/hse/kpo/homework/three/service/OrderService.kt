package se.hse.kpo.homework.three.service

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import se.hse.kpo.homework.three.entity.Order
import se.hse.kpo.homework.three.repository.OrderRepository
import se.hse.kpo.homework.three.repository.StationRepository
import java.sql.Timestamp
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class OrderService(private val orderRepository: OrderRepository,
                   private val stationRepository: StationRepository,
                   private val restTemplate: RestTemplate
) {
    fun createOrder(token: String, fromStationId: Long, toStationId: Long): String {
        // WORK WITH TOKEN

        if (token.isEmpty()) {
            throw Exception("Invalid token")
        }
        if (stationRepository.findById(fromStationId).getOrNull() == null) {
            throw Exception("No station with from_station_id")
        }
        if (stationRepository.findById(toStationId).getOrNull() == null) {
            throw Exception("No station with to_station_id")
        }
        // localhost <=> regService
        val id: Long? = restTemplate.getForObject("http://regService:8080/api/get/${token}", Long::class.java)
        if (id == null || id.toInt() == -1) {
            throw Exception("Invalid token")
        }

        val order = Order(null, id, fromStationId, toStationId, 1, Timestamp(Date().time))
        orderRepository.save(order)

        return "Order created successfully. Id: " + order.id
    }

    fun getOrderById(id: Long): String {
        try {
            // Check order existence
            val order = orderRepository.findById(id).getOrNull() ?: throw Exception("No order with such id")

            // Get the order by id
            return ("id: " + order.id + "\nuser_id: " + order.userId +
                    "\nfrom_station: (" + order.fromStationId + ", " +
                    stationRepository.findById(order.fromStationId).getOrNull()?.station + ")" +
                    "\nto_station: (" + order.toStationId + ", " +
                    stationRepository.findById(order.toStationId).getOrNull()?.station + ")" +
                    "\nstatus: " + order.status + "\ncreated: " + order.created)
        } catch (ex: Exception) {
            throw ex
        }
    }
}