package com.springleaf.object.entity;

import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post extends Model {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    Boolean active = true;

    @Column(nullable = false)
    Date create_date = new Date();

    @OneToOne
    Language language;

    @ManyToOne
    User user;

    @ManyToOne
    Topic topic;

    @OneToMany
    List<Comment> comments;

    @OneToMany
    List<Vote> votes;
}
