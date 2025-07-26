package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.*;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Role;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.User;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Vehicle;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.FuelBook_TypeRepo;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.RoleRepo;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.UsersRepo;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.VehicleRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private VehicleRepo vehicleRepo;
    @Autowired
    private FuelBook_TypeRepo fuelBookTypeRepo;



    private final EmailService emailService;

    private static final String ADMIN_EMAIL = "admin@gmail.com";
    private static final String ADMIN_PASSWORD = "admin@123";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";

    private static final String FUEL_EMAIL = "fuel@gmail.com";
    private static final String FUEL_PASSWORD = "fuel@123";
    private static final String ROLE_FUEL_ATTENDANT = "FUEL ATTENDANT";
    private static final Long CID = Long.valueOf("10022010212"); // Admin
    private static final Long FUEL_CID = Long.valueOf("10022010213"); // Different for fuel user



    public UsersManagementService(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostConstruct
    public void initAdminUser() {
        if (usersRepo.findByEmail(ADMIN_EMAIL).isEmpty()) {
            Role adminRole = roleRepo.findByRoleType(ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleType(ROLE_ADMIN);
                        role.setDescription("Administrator role");
                        return roleRepo.save(role);
                    });

            User adminUser = new User();
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            adminUser.setUsername("Admin User");
            adminUser.setPhone_no("1234567890");
            adminUser.setCid(CID);
            adminUser.setRole(adminRole);

            usersRepo.save(adminUser);
            System.out.println("Admin user created with email: " + ADMIN_EMAIL);
        }
    }


    @PostConstruct
    public void initFuelUser() {
        if (usersRepo.findByEmail(FUEL_EMAIL).isEmpty()) {
            Role fuleRole = roleRepo.findByRoleType(ROLE_FUEL_ATTENDANT)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleType(ROLE_FUEL_ATTENDANT);
                        role.setDescription("Fuel Attendant role");
                        return roleRepo.save(role);
                    });

            User fuelUser = new User();
            fuelUser.setEmail(FUEL_EMAIL);
            fuelUser.setPassword(passwordEncoder.encode(FUEL_PASSWORD));
            fuelUser.setUsername("Fuel User");
            fuelUser.setPhone_no("177890");
            fuelUser.setRole(fuleRole);
            fuelUser.setCid(FUEL_CID);

            usersRepo.save(fuelUser);
            System.out.println("Fuel user created with email: " + FUEL_EMAIL);
        }
    }

//    public ReqRes register(ReqRes registrationRequest) {
//        ReqRes resp = new ReqRes();
//        try {
//            Optional<User> existingUser = usersRepo.findByEmail(registrationRequest.getEmail());
//            if (existingUser.isPresent()) {
//                resp.setStatusCode(400);
//                resp.setMessage("A user with this email already exists.");
//                return resp;
//            }
//
//            Role role = roleRepo.findById(registrationRequest.getRoleId())
//                    .orElseThrow(() -> new RuntimeException("Role not found"));
//
//            User user = new User();
//            user.setEmail(registrationRequest.getEmail());
//            user.setUsername(registrationRequest.getUsername());
//            user.setPhone_no(registrationRequest.getPhone_no());
//            user.setCid(registrationRequest.getCid());
//            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
//            user.setRole(role);
//
//            String otp = generateOTP();
//            user.setOtp(otp);
//            usersRepo.save(user);
//
//            sendVerificationEmail(user.getEmail(), otp);
//            resp.setMessage("User Registered Successfully");
//            resp.setStatusCode(200);
//
//        } catch (Exception e) {
//            resp.setStatusCode(500);
//            resp.setError(e.getMessage());
//        }
//        return resp;
//    }
//
//    private String generateOTP() {
//        Random random = new Random();
//        int otpValue = 100000 + random.nextInt(900000);
//        return String.valueOf(otpValue);
//    }


    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            Optional<User> existingUser = usersRepo.findByEmail(registrationRequest.getEmail());
            if (existingUser.isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("A user with this email already exists.");
                return resp;
            }
            Role role = roleRepo.findById(registrationRequest.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            Vehicle vehicle = vehicleRepo.findById(registrationRequest.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));



            // ✅ Create new user
            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setUsername(registrationRequest.getUsername());
            user.setPhone_no(registrationRequest.getPhone_no());
            user.setCid(registrationRequest.getCid());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setVehicle(vehicle);
            user.setRole(role);

            // ✅ Generate and send OTP
            String otp = generateOTP();
            user.setOtp(otp);
            usersRepo.save(user);
            resp.setMessage("User Registered Successfully");
            resp.setStatusCode(200);

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }

        return resp;
    }
    private String generateOTP() {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }



    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            User user = usersRepo.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user.getOtp() != null && user.getOtp().equals(loginRequest.getPassword())) {
                String newOtp = generateOTP();
                user.setOtp(newOtp);
                usersRepo.save(user);

                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), user.getAuthorities()
                );

                String jwt = jwtUtils.generateToken(userDetails, Math.toIntExact(user.getId()), user.getRole().getRoleType());
                String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userDetails);

                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshToken);
                response.setExpirationTime("24Hrs");
                response.setMessage("Successfully Logged In with OTP");
            } else if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        user.getEmail(), user.getPassword(), user.getAuthorities()
                );

                String jwt = jwtUtils.generateToken(userDetails, Math.toIntExact(user.getId()), user.getRole().getRoleType());
                String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), userDetails);

                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshToken);
                response.setExpirationTime("24Hrs");

                if (ROLE_ADMIN.equals(user.getRole().getRoleType())) {
                    response.setMessage("Admin successfully logged in");
                } else {
                    response.setMessage("User successfully logged in");
                }
            } else {
                response.setStatusCode(401);
                response.setMessage("Invalid OTP or Password");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        ReqRes resp = new ReqRes();
        try {
            if (!updatePasswordDTO.getNewPassword().equals(updatePasswordDTO.getConfirmPassword())) {
                resp.setStatusCode(400);
                resp.setMessage("Error: New password and confirm password do not match.");
                return resp;
            }
            User user = usersRepo.findByEmail(updatePasswordDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setOtp(null);
            user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
            usersRepo.save(user);

            resp.setStatusCode(200);
            resp.setMessage("Password updated successfully. You can now log in using your new password.");

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error: " + e.getMessage());
        }
        return resp;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String token = refreshTokenRequest.getToken();
            String email = jwtUtils.extractUsername(token);
            Integer userId = jwtUtils.extractUserId(token);
            String role = jwtUtils.extractRole(token);

            User user = usersRepo.findByEmail(email).orElseThrow();
            if (jwtUtils.isTokenValid(token, user)) {
                var newJwt = jwtUtils.generateToken(user, userId, role);
                response.setStatusCode(200);
                response.setToken(newJwt);
                response.setRefreshToken(token);
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            } else {
                response.setStatusCode(401);
                response.setMessage("Invalid Token");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public ReqRes assignRoleToUser(Long userId, Long roleId) {
        ReqRes response = new ReqRes();
        try {
            User user = usersRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

            Role role = roleRepo.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found with ID: " + roleId));

            user.setRole(role);
            usersRepo.save(user);

            response.setStatusCode(200);
            response.setMessage("Role assigned successfully to user ID: " + userId);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes getUserById(Long userId) {
        ReqRes response = new ReqRes();
        try {
            User user = usersRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ReqRes userDetails = new ReqRes();
            userDetails.setUserId(user.getId());
            userDetails.setUsername(user.getUsername());
            userDetails.setPhone_no(user.getPhone_no());
            userDetails.setEmail(user.getEmail());
            userDetails.setCid(user.getCid());
            userDetails.setRoleId(user.getRole().getRoleId()); // Single role now

            List<ReqRes> userDetailsList = new ArrayList<>();
            userDetailsList.add(userDetails);

            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
            response.setUserDetailsList(userDetailsList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }


    public ReqRes getAllUser() {
        ReqRes response = new ReqRes();
        try {
            List<User> users = usersRepo.findAll();

            if (users.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("No users found.");
            } else {
                response.setStatusCode(200);
                List<ReqRes> userDetailsList = new ArrayList<>();

                for (User user : users) {
                    ReqRes userDetails = new ReqRes();
                    // Basic user info
                    userDetails.setUserId(user.getId());
                    userDetails.setUsername(user.getUsername());
                    userDetails.setPhone_no(user.getPhone_no());
                    userDetails.setEmail(user.getEmail());
                    userDetails.setCid(user.getCid());

                    // Role details
                    if (user.getRole() != null) {
                        userDetails.setRoleId(user.getRole().getRoleId());
                        userDetails.setRoleType(user.getRole().getRoleType());
                    }

                    // Vehicle details
                    if (user.getVehicle() != null) {
                        userDetails.setVehicleId(user.getVehicle().getId());
                        VehicleDTO vehicleDTO = new VehicleDTO();
                        vehicleDTO.setId(user.getVehicle().getId());
                        vehicleDTO.setRegistrationNumber(user.getVehicle().getRegistrationNumber());

                        // Company details inside vehicle
                        if (user.getVehicle().getCompany() != null) {
                            CompanyDTO companyDTO = new CompanyDTO();
                            companyDTO.setId(user.getVehicle().getCompany().getId());
                            companyDTO.setName(user.getVehicle().getCompany().getName());
                            companyDTO.setAddress(user.getVehicle().getCompany().getAddress());
                            companyDTO.setContactPerson(user.getVehicle().getCompany().getContactPerson());
                            companyDTO.setContactEmail(user.getVehicle().getCompany().getContactEmail());
                            companyDTO.setContactPhone(user.getVehicle().getCompany().getContactPhone());
                            vehicleDTO.setCompany(companyDTO);
                            vehicleDTO.setCompanyId(user.getVehicle().getCompany().getId());
                        }

                        userDetails.setVehicle(vehicleDTO);

                        // FuelType information from vehicle's fuelBook_type
                        if (user.getVehicle().getFuelBook_type() != null) {
                            FuelBook_TypeDTO fuelTypeDTO = new FuelBook_TypeDTO();
                            fuelTypeDTO.setId(user.getVehicle().getFuelBook_type().getId());
                            fuelTypeDTO.setFuel_type(user.getVehicle().getFuelBook_type().getFuel_type());
                            // Set other fuel type fields as needed
                            userDetails.setFuelType(fuelTypeDTO);
                        }
                    }

                    userDetailsList.add(userDetails);
                }

                response.setMessage("Users fetched successfully");
                response.setUserDetailsList(userDetailsList);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

    public ReqRes updateEmailAndUsername(Long userId, String email, String username) {
        ReqRes response = new ReqRes();
        try {
            Optional<User> userOptional = usersRepo.findById(userId);
            if (userOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("No user found.");
                return response;
            }

            User user = userOptional.get();
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
            if (username != null && !username.isEmpty()) {
                user.setUsername(username);
            }

            usersRepo.save(user);

            response.setStatusCode(200);
            response.setMessage("User details updated successfully");

            List<ReqRes> userDetailsList = new ArrayList<>();
            ReqRes userDetails = new ReqRes();
            userDetails.setUserId(user.getId());
            userDetails.setUsername(user.getUsername());
            userDetails.setPhone_no(user.getPhone_no());
            userDetails.setEmail(user.getEmail());
            userDetails.setCid(user.getCid());
            userDetailsList.add(userDetails);
            response.setUserDetailsList(userDetailsList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }
}