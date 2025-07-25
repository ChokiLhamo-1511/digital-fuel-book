package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.VehicleDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.JWTUtils;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.VehicleService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private JWTUtils jwtUtils;

    private ResponseEntity<?> checkAdminAuthorization(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String role = jwtUtils.extractRole(token);

            if (!"ADMIN".equals(role)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Insufficient privileges to perform this action");
            }
            return null;
        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Session expired. Please login again");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + ex.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createVehicle(@RequestBody VehicleDTO vehicleDTO,
                                           HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
            return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create vehicle: " + ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllVehicles(HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(vehicles);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch vehicles: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            VehicleDTO vehicle = vehicleService.getVehicleById(id);
            if (vehicle != null) {
                return ResponseEntity.ok(vehicle);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Vehicle not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch vehicle: " + ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id,
                                           @RequestBody VehicleDTO vehicleDTO,
                                           HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
            if (updatedVehicle != null) {
                return ResponseEntity.ok(updatedVehicle);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Vehicle not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update vehicle: " + ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            boolean isDeleted = vehicleService.deleteVehicle(id);
            if (isDeleted) {
                return ResponseEntity.ok("Vehicle deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Vehicle not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete vehicle: " + ex.getMessage());
        }
    }
}