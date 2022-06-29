package com.springleaf.object.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.springleaf.object.entity.types.UserStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User extends Model {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String first_name;

    @Column(nullable = false)
    String last_name;

    @Column(nullable = false)
    Date dob;

    @Column(nullable = false)
    String email;

    @Column(nullable = true)
    String biography;

    @Enumerated(EnumType.STRING)
    UserStatus status;

    @Column(nullable = false)
    Boolean active = true;

    @ManyToMany
    List<Role> roles;

    @Column(nullable = true)
    Long last_change_password;

    @OneToOne
    Login login;
}
