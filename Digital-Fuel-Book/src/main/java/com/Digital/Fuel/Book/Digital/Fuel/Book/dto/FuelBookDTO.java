package com.Digital.Fuel.Book.Digital.Fuel.Book.dto;

import jakarta.persistence.metamodel.EntityType;
import lombok.Data;

@Data
public class FuelBookDTO {
    private Long id;
    private double initialBalance;
    private double currentBalance;
    private double thresholdAmount;
    private Long vehicleId;
    private VehicleDTO vehicle;
    private Long companyId;
    private CompanyDTO company;




}
