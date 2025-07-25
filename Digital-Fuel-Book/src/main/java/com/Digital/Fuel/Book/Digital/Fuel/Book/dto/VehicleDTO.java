package com.Digital.Fuel.Book.Digital.Fuel.Book.dto;

import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Vehicle;
import lombok.Data;

@Data
public class VehicleDTO {

    private Long id;
    private String registrationNumber;
    private Long companyId;
    private CompanyDTO company;
    private Long fuelBookId;
    private FuelBookDTO fuelBook;
    private Long fuelBookTypeId;
    private FuelBook_TypeDTO fuelBookType;





}
