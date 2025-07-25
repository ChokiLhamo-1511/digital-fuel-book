package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.FuelBookDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.FuelBookService;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.JWTUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/fuel-books")
public class FuelBookController {

    @Autowired
    private FuelBookService fuelBookService;

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
    public ResponseEntity<?> createFuelBook(@RequestBody FuelBookDTO fuelBookDTO, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            FuelBookDTO createdFuelBook = fuelBookService.createFuelBook(fuelBookDTO);
            return new ResponseEntity<>(createdFuelBook, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating fuel book: " + ex.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<FuelBookDTO> getFuelBookById(@PathVariable Long id) {
        FuelBookDTO fuelBookDTO = fuelBookService.getFuelBookById(id);
        return ResponseEntity.ok(fuelBookDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FuelBookDTO>> getAllFuelBooks() {
        List<FuelBookDTO> fuelBooks = fuelBookService.getAllFuelBooks();
        return ResponseEntity.ok(fuelBooks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelBookDTO> updateFuelBook(
            @PathVariable Long id,
            @RequestBody FuelBookDTO fuelBookDTO) {
        FuelBookDTO updatedFuelBook = fuelBookService.updateFuelBook(id, fuelBookDTO);
        return ResponseEntity.ok(updatedFuelBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelBook(@PathVariable Long id) {
        fuelBookService.deleteFuelBook(id);
        return ResponseEntity.noContent().build();
    }
}