package com.Digital.Fuel.Book.Digital.Fuel.Book.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;


    @Column(length = 255, nullable = false)
    private String description;

    @Column(name="role_type")
    private String roleType;


    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();


}