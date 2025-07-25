package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuelBookRepo extends JpaRepository<FuelBook,Long> {
    Optional<FuelBook> findTopByOrderByIdDesc();
    Optional<FuelBook> findByVehicleId(Long vehicleId);


}
