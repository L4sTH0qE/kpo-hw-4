package se.hse.kpo.homework.three.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import se.hse.kpo.homework.three.entity.Station

@Repository
interface StationRepository: JpaRepository<Station, Long> {

}