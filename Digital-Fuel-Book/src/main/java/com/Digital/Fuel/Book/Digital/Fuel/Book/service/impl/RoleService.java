package com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.RoleDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.entity.Role;
import com.Digital.Fuel.Book.Digital.Fuel.Book.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setDescription(roleDTO.getDescription());
        role.setRoleType(roleDTO.getRoleType());

        Role savedRole = roleRepo.save(role);
        return convertToDTO(savedRole);
    }

    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.setDescription(roleDTO.getDescription());
            role.setRoleType(roleDTO.getRoleType());

            Role updatedRole = roleRepo.save(role);
            return convertToDTO(updatedRole);
        }
        return null; // or throw exception
    }

    public RoleDTO getRoleById(Long roleId) {
        Optional<Role> optionalRole = roleRepo.findById(roleId);
        return optionalRole.map(this::convertToDTO).orElse(null);
    }

    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return roles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteRole(Long roleId) {
        roleRepo.deleteById(roleId);
    }

    public RoleDTO findByRoleType(String roleType) {
        Optional<Role> optionalRole = roleRepo.findByRoleType(roleType);
        return optionalRole.map(this::convertToDTO).orElse(null);
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleId(role.getRoleId());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setRoleType(role.getRoleType());
        return roleDTO;
    }

}
