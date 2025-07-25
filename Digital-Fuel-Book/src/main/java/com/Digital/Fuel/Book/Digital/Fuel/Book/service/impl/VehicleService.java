package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.CompanyDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBookDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBook_TypeDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.VehicleDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.*;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private CompanyRepo companyRepo;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private FuelBookRepo fuelBookRepo;

    @Autowired
    private FuelBook_TypeRepo fuelBook_typeRepo;

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        // Validate and get company
        Company company = companyRepo.findById(vehicleDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + vehicleDTO.getCompanyId()));

        // Create vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(vehicleDTO.getRegistrationNumber());
        vehicle.setCompany(company);

        // Set fuel book if provided
        if (vehicleDTO.getFuelBookId() != null) {
            FuelBook fuelBook = fuelBookRepo.findById(vehicleDTO.getFuelBookId())
                    .orElseThrow(() -> new RuntimeException("Fuel book not found with id: " + vehicleDTO.getFuelBookId()));
            vehicle.setFuelBook(fuelBook);
        }
        if (vehicleDTO.getFuelBookTypeId() != null) {
            FuelBook_Type fuelBookType = fuelBook_typeRepo.findById(vehicleDTO.getFuelBookTypeId())
                    .orElseThrow(() -> new RuntimeException("Fuel book type not found with id: " + vehicleDTO.getFuelBookTypeId()));
            vehicle.setFuelBook_type(fuelBookType);
        }

        Vehicle savedVehicle = vehicleRepo.save(vehicle);
        return toVehicleDTO(savedVehicle);
    }

    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepo.findAll();
        return vehicles.stream()
                .map(this::toVehicleDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        return vehicleRepo.findById(id)
                .map(this::toVehicleDTO)
                .orElse(null);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        return vehicleRepo.findById(id)
                .map(vehicle -> {
                    // Update basic fields
                    vehicle.setRegistrationNumber(vehicleDTO.getRegistrationNumber());

                    // Update company if changed
                    if (!vehicle.getCompany().getId().equals(vehicleDTO.getCompanyId())) {
                        Company company = companyRepo.findById(vehicleDTO.getCompanyId())
                                .orElseThrow(() -> new RuntimeException("Company not found"));
                        vehicle.setCompany(company);
                    }

                    // Update fuel book if changed
                    if (vehicleDTO.getFuelBookId() != null &&
                            (vehicle.getFuelBook() == null || !vehicle.getFuelBook().getId().equals(vehicleDTO.getFuelBookId()))) {
                        FuelBook fuelBook = fuelBookRepo.findById(vehicleDTO.getFuelBookId())
                                .orElseThrow(() -> new RuntimeException("Fuel book not found"));
                        vehicle.setFuelBook(fuelBook);
                    }

                    Vehicle updatedVehicle = vehicleRepo.save(vehicle);
                    return toVehicleDTO(updatedVehicle);
                })
                .orElse(null);
    }

    public boolean deleteVehicle(Long id) {
        if (vehicleRepo.existsById(id)) {
            vehicleRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private VehicleDTO toVehicleDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setCompanyId(vehicle.getCompany().getId());

        // Set company details
        if (vehicle.getCompany() != null) {
            CompanyDTO companyDto = new CompanyDTO();
            companyDto.setId(vehicle.getCompany().getId());
            companyDto.setName(vehicle.getCompany().getName());
            companyDto.setAddress(vehicle.getCompany().getAddress());
            companyDto.setContactPerson(vehicle.getCompany().getContactPerson());
            companyDto.setContactEmail(vehicle.getCompany().getContactEmail());
            companyDto.setContactPhone(vehicle.getCompany().getContactPhone());
            dto.setCompany(companyDto);
        }

        // Set fuel book type details if exists
        if (vehicle.getFuelBook_type() != null) {
            dto.setFuelBookTypeId(vehicle.getFuelBook_type().getId());

            FuelBook_TypeDTO typeDTO = new FuelBook_TypeDTO();
            typeDTO.setId(vehicle.getFuelBook_type().getId());
            typeDTO.setFuel_type(vehicle.getFuelBook_type().getFuel_type());
            dto.setFuelBookType(typeDTO);
        }

        return dto;
    }
}