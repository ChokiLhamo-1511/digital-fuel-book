package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.CompanyDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Company;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setContactPerson(companyDTO.getContactPerson());
        company.setContactEmail(companyDTO.getContactEmail());
        company.setContactPhone(companyDTO.getContactPhone());

        Company savedCompany = companyRepo.save(company);

        return convertToDTO(savedCompany);
    }

    public List<CompanyDTO> getAllCompanies() {
        List<Company> companies = companyRepo.findAll();
        return companies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CompanyDTO getCompanyById(Long id) {
        Optional<Company> companyOptional = companyRepo.findById(id);
        return companyOptional.map(this::convertToDTO).orElse(null);
    }

    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Optional<Company> companyOptional = companyRepo.findById(id);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            company.setName(companyDTO.getName());
            company.setAddress(companyDTO.getAddress());
            company.setContactPerson(companyDTO.getContactPerson());
            company.setContactEmail(companyDTO.getContactEmail());
            company.setContactPhone(companyDTO.getContactPhone());

            Company updatedCompany = companyRepo.save(company);
            return convertToDTO(updatedCompany);
        }
        return null;
    }

    public boolean deleteCompany(Long id) {
        if (companyRepo.existsById(id)) {
            companyRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        dto.setContactPerson(company.getContactPerson());
        dto.setContactEmail(company.getContactEmail());
        dto.setContactPhone(company.getContactPhone());
        return dto;
    }
}