package com.Digital.Fuel.Book.Digital.Fuel.Book.controller;

import com.Digital.Fuel.Book.Digital.Fuel.Book.dto.RoleDTO;
import com.Digital.Fuel.Book.Digital.Fuel.Book.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
        RoleDTO updatedRole = roleService.updateRole(roleId, roleDTO);
        if (updatedRole != null) {
            return ResponseEntity.ok(updatedRole);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long roleId) {
        RoleDTO roleDTO = roleService.getRoleById(roleId);
        if (roleDTO != null) {
            return ResponseEntity.ok(roleDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{roleType}")
    public ResponseEntity<RoleDTO> getRoleByType(@PathVariable String roleType) {
        RoleDTO roleDTO = roleService.findByRoleType(roleType);
        if (roleDTO != null) {
            return ResponseEntity.ok(roleDTO);
        }
        return ResponseEntity.notFound().build();
    }
}