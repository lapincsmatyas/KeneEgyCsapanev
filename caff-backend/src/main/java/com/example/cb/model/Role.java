package com.example.cb.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum name;

    public Role() { }

    public Role(RoleEnum role) {
        this.name = role;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public RoleEnum getRole() {
        return name;
    }

    public void setRole(RoleEnum role) {
        this.name = role;
    }

}
