package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook_Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelBook_TypeRepo extends JpaRepository<FuelBook_Type, Long> {
}
