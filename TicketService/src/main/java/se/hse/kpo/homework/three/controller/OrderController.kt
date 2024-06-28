package se.hse.kpo.homework.three.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import se.hse.kpo.homework.three.dto.CreateDTO
import se.hse.kpo.homework.three.dto.OrderDTO
import se.hse.kpo.homework.three.service.OrderService

@RestController
@RequestMapping("/order")
class OrderController(val orderService: OrderService) {

    @PostMapping("/create")
    fun register(@RequestHeader("Authorization") authHeader: String?, @RequestBody createDTO: CreateDTO): ResponseEntity<String> {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        // Проверка входных данных на корректность
        if (createDTO.fromStationId == null || createDTO.toStationId == null) {
            return ResponseEntity.badRequest().body("Invalid stations")
        }

        return try {
            ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(authHeader.substring(7),
                createDTO.fromStationId, createDTO.toStationId))
        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(ex.message)
        }
    }


    @GetMapping("/get")
    fun getInfo(@RequestBody orderDTO: OrderDTO): ResponseEntity<String> {

        if (orderDTO.id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid order index");
        }

        return try {
            ResponseEntity.ok(orderService.getOrderById(orderDTO.id))
        } catch (ex: Exception) {
            ResponseEntity.badRequest().body(ex.message)
        }
    }
}