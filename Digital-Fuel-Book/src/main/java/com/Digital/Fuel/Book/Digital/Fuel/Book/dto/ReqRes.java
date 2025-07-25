package com.Digital.Fuel.Book.Digital.Fuel.Book.dto;


import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Role;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String username;
    private String email;
    private Long userId;
    private String password;
    private String phone_no;
    private Long cid;
    private List<ReqRes> userDetailsList;
    private Long vehicleId;
    private Long roleId;
    private String roleType;


    private Long companyId;
    private Long fuelTypeId;

    private CompanyDTO company;
    private FuelBook_TypeDTO fuelType;
    private VehicleDTO vehicle;
    private FuelBookDTO fuelBook;


}
