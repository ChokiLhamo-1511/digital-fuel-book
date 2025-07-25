package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.CompanyDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBookDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBook_TypeDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.VehicleDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Company;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook_Type;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Vehicle;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuelBookService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private FuelBookRepo fuelBookRepo;
    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private FuelBook_TypeRepo fuelBookTypeRepo;



    private final EmailService emailService;

    public FuelBookService(EmailService emailService) {
        this.emailService = emailService;
    }

    public FuelBookDTO createFuelBook(FuelBookDTO fuelBookDTO) {
        Vehicle vehicle = vehicleRepo.findById(fuelBookDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found with id: " + fuelBookDTO.getVehicleId()));

        Company company = companyRepo.findById(fuelBookDTO.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + fuelBookDTO.getCompanyId()));

        FuelBook fuelBook = new FuelBook();
        fuelBook.setInitialBalance(fuelBookDTO.getInitialBalance());  // Set initial balance
        fuelBook.setCurrentBalance(fuelBookDTO.getCurrentBalance());
        fuelBook.setThresholdAmount(fuelBookDTO.getThresholdAmount());
        fuelBook.setVehicle(vehicle);
        fuelBook.setCompany(company);

        FuelBook savedFuelBook = fuelBookRepo.save(fuelBook);
        return toFuelBookDTO(savedFuelBook);
    }

    public FuelBookDTO getFuelBookById(Long id) {
        FuelBook fuelBook = fuelBookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel book not found with id: " + id));
        return toFuelBookDTO(fuelBook);
    }

    public List<FuelBookDTO> getAllFuelBooks() {
        List<FuelBook> fuelBooks = fuelBookRepo.findAll();
        return fuelBooks.stream().map(this::toFuelBookDTO).collect(Collectors.toList());
    }

    public FuelBookDTO updateFuelBook(Long id, FuelBookDTO fuelBookDTO) {
        FuelBook fuelBook = fuelBookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel book not found with id: " + id));

        if (fuelBookDTO.getCurrentBalance() != 0) {
            fuelBook.setCurrentBalance(fuelBookDTO.getCurrentBalance());
        }
        if (fuelBookDTO.getThresholdAmount() != 0) {
            fuelBook.setThresholdAmount(fuelBookDTO.getThresholdAmount());
        }

        FuelBook updatedFuelBook = fuelBookRepo.save(fuelBook);
        return toFuelBookDTO(updatedFuelBook);
    }

    public void deleteFuelBook(Long id) {
        FuelBook fuelBook = fuelBookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Fuel book not found with id: " + id));
        fuelBookRepo.delete(fuelBook);
    }

    private FuelBookDTO toFuelBookDTO(FuelBook fuelBook) {
        FuelBookDTO dto = new FuelBookDTO();
        dto.setId(fuelBook.getId());
        dto.setInitialBalance(fuelBook.getInitialBalance());
        dto.setCurrentBalance(fuelBook.getCurrentBalance());
        dto.setThresholdAmount(fuelBook.getThresholdAmount());



        if (fuelBook.getVehicle() != null) {
            dto.setVehicleId(fuelBook.getVehicle().getId());

            // Create and populate VehicleDTO
            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setId(fuelBook.getVehicle().getId());
            vehicleDTO.setRegistrationNumber(fuelBook.getVehicle().getRegistrationNumber());
            vehicleDTO.setCompanyId(fuelBook.getVehicle().getCompany().getId());
            dto.setVehicle(vehicleDTO);
        }

        if (fuelBook.getCompany() != null) {
            dto.setCompanyId(fuelBook.getCompany().getId());
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.setId(fuelBook.getCompany().getId());
            companyDTO.setName(fuelBook.getCompany().getName());
            dto.setCompany(companyDTO);  // Fixed: Changed from setVehicle to setCompany
        }

        return dto;
    }
}
