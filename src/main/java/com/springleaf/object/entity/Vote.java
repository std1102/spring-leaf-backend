package com.springleaf.object.entity;

import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.springleaf.object.entity.types.VoteType;

import javax.persistence.*;
import java.util.Date;

@Table(name = "vote")
@Entity
@Getter
@Setter
public class Vote extends Model {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    VoteType type;

    @Column(nullable = false)
    Date time = new Date();

    @ManyToOne
    User user;

    @ManyToOne
    Comment comment;

    @ManyToOne
    Post post;
}
