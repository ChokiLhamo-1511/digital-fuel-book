package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;


import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.role LEFT JOIN FETCH u.vehicle")
    List<User> findAllWithRoleAndVehicle();






}
