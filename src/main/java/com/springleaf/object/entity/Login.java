package com.springleaf.object.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "login")
@Getter
@Setter
public class Login extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    Long user_id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    String token;

    @OneToOne
    User user;
}
