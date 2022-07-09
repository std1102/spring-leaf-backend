package com.springleaf.object.entity;

import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment extends Model {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    Date time = new Date();

    @Column(nullable = false)
    String content;

    @ManyToOne
    User user;

    @Column(nullable = false)
    Boolean active = true;

    @ManyToOne
    Comment parent;

    @ManyToOne
    Post post;
}
