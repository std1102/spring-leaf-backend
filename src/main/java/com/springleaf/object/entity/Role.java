package com.springleaf.object.entity;

import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.springleaf.object.entity.types.RoleType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RoleType type;

    @Column(nullable = false)
    Boolean active = true;

    @ManyToMany
    List<User> users;
}
