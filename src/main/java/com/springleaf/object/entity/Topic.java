package com.springleaf.object.entity;

import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "topic")
@Getter
@Setter
public class Topic extends Model {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Boolean active = true;

    @Column(nullable = false)
    String name;

    @ManyToOne
    Subject subject;

    @OneToMany
    List<Post> posts;
}
