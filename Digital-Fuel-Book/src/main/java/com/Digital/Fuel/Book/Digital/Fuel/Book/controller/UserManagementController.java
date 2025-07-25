package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;


import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.ReqRes;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.RoleDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.UpdatePasswordDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.UsersRepo;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.EmailService;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.JWTUtils;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.UsersManagementService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsersRepo usersRepo;


    @Autowired
    private JWTUtils jwtUtils;



    @PostMapping("/api/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg, HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authHeader.substring(7);
            String role = jwtUtils.extractRole(token);

            if (!"ADMIN".equals(role)) {
                ReqRes errorResponse = new ReqRes();
                errorResponse.setMessage("You do not have the necessary permissions to create an agency type.");
                errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            }

            ReqRes response = usersManagementService.register(reg);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/api/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {
        ReqRes response = usersManagementService.login(req);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PostMapping("/api/auth/updatePassword/{userId}")
    public ResponseEntity<ReqRes> updatePassword(@PathVariable Long userId, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        ReqRes response = usersManagementService.updatePassword(updatePasswordDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/api/auth/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable Long userId) {
        ReqRes response = usersManagementService.getUserById(userId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/api/auth/getAllUser")
    public ResponseEntity<ReqRes> getAllUsers() {
        ReqRes response = usersManagementService.getAllUser();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/api/auth/update")
    public ResponseEntity<ReqRes> updateEmailAndUsername(
            @RequestParam Long userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {

        ReqRes response = usersManagementService.updateEmailAndUsername(userId, email, username);

        if (response.getStatusCode() == 200) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

}



