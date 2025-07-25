package com.Digital.Fuel.Book.Digital.Fuel.Book.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CompanyDTO {

    private Long id;
    private String name;
    private String address;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
}
