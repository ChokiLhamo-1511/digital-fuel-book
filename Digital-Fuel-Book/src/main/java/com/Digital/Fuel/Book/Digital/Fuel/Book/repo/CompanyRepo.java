package com.Digital.Fuel.Book.Digital.Fuel.Book.repo;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company,Long> {
}
