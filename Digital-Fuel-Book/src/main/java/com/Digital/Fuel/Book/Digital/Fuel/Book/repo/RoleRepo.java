package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Role;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    @Query("SELECT r FROM Role r WHERE r.description = :description")
    Optional<Role> findByRoleName(@Param("description") String description);

    Optional<Role> findByRoleId(Long roleId);

    Optional<Role> findByRoleType(String user);


}
