package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBook_TypeDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.FuelBook_Type;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.FuelBook_TypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FuelBook_TypeService {

    @Autowired
    private FuelBook_TypeRepo fuelBook_typeRepo;

    public FuelBook_TypeDTO createFuelBook_Type(FuelBook_TypeDTO fuelBook_typeDTO) {
        FuelBook_Type fuelBook_type = new FuelBook_Type();
        fuelBook_type.setFuel_type(fuelBook_typeDTO.getFuel_type());

        FuelBook_Type savedFuelBookType = fuelBook_typeRepo.save(fuelBook_type);
        return convertToDTO(savedFuelBookType);
    }

    public List<FuelBook_TypeDTO> getAllFuelBookTypes() {
        List<FuelBook_Type> types = fuelBook_typeRepo.findAll();
        return types.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FuelBook_TypeDTO getFuelBookTypeById(Long id) {
        Optional<FuelBook_Type> typeOptional = fuelBook_typeRepo.findById(id);
        return typeOptional.map(this::convertToDTO).orElse(null);
    }

    public FuelBook_TypeDTO updateFuelBookType(Long id, FuelBook_TypeDTO fuelBook_typeDTO) {
        Optional<FuelBook_Type> typeOptional = fuelBook_typeRepo.findById(id);
        if (typeOptional.isPresent()) {
            FuelBook_Type fuelBookType = typeOptional.get();
            fuelBookType.setFuel_type(fuelBook_typeDTO.getFuel_type());

            FuelBook_Type updatedType = fuelBook_typeRepo.save(fuelBookType);
            return convertToDTO(updatedType);
        }
        return null;
    }

    public boolean deleteFuelBookType(Long id) {
        if (fuelBook_typeRepo.existsById(id)) {
            fuelBook_typeRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private FuelBook_TypeDTO convertToDTO(FuelBook_Type fuelBookType) {
        FuelBook_TypeDTO dto = new FuelBook_TypeDTO();
        dto.setId(fuelBookType.getId());
        dto.setFuel_type(fuelBookType.getFuel_type());
        return dto;
    }
}