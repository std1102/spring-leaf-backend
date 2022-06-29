package com.springleaf.object.entity;

import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
@Getter
@Setter
public class Subject extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Boolean active = true;

    @OneToMany
    List<Topic> topics;
}
