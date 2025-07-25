package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.CompanyDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.ReqRes;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.UsersRepo;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.CompanyService;
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
@RequestMapping("/api/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

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
    public ResponseEntity<?> createCompany(@RequestBody CompanyDTO companyDTO, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            CompanyDTO createdCompany = companyService.createCompany(companyDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while creating company: " + ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies(HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            List<CompanyDTO> companies = companyService.getAllCompanies();
            return ResponseEntity.ok(companies);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching companies: " + ex.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            CompanyDTO company = companyService.getCompanyById(id);
            if (company != null) {
                return ResponseEntity.ok(company);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching company: " + ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDTO, HttpServletRequest request) {
        ResponseEntity<?> authResponse = checkAdminAuthorization(request);
        if (authResponse != null) return authResponse;

        try {
            CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
            if (updatedCompany != null) {
                return ResponseEntity.ok(updatedCompany);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found with id: " + id);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating company: " + ex.getMessage());
        }
    }


}