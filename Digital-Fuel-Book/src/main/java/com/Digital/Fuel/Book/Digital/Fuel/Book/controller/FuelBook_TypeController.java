package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBook_TypeDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.ReqRes;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.FuelBook_TypeService;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/fuel-book-type")
public class FuelBook_TypeController {

    @Autowired
    private FuelBook_TypeService fuelBookTypeService;

    @Autowired
    private JWTUtils jwtUtils;

    private ResponseEntity<?> checkAdminAuthorization(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String role = jwtUtils.extractRole(token);

            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have the necessary permissions to perform this action.");
            }
            return null;
        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT Token has expired");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFuelBookType(@RequestBody FuelBook_TypeDTO fuelBookTypeDTO, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            FuelBook_TypeDTO createdFuelBookType = fuelBookTypeService.createFuelBook_Type(fuelBookTypeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFuelBookType);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating fuel book type: " + ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllFuelBookTypes(HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            List<FuelBook_TypeDTO> types = fuelBookTypeService.getAllFuelBookTypes();
            return ResponseEntity.ok(types);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching fuel book types: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFuelBookTypeById(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            FuelBook_TypeDTO type = fuelBookTypeService.getFuelBookTypeById(id);
            if (type != null) {
                return ResponseEntity.ok(type);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fuel book type not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching fuel book type: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFuelBookType(@PathVariable Long id, @RequestBody FuelBook_TypeDTO fuelBookTypeDTO,
                                                HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            FuelBook_TypeDTO updatedType = fuelBookTypeService.updateFuelBookType(id, fuelBookTypeDTO);
            if (updatedType != null) {
                return ResponseEntity.ok(updatedType);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fuel book type not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating fuel book type: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFuelBookType(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            boolean isDeleted = fuelBookTypeService.deleteFuelBookType(id);
            if (isDeleted) {
                return ResponseEntity.ok("Fuel book type deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fuel book type not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting fuel book type: " + ex.getMessage());
        }
    }
}