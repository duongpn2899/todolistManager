package com.example.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Getter
@Setter
@Table(name = "todo")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column
    private Instant created;

    @Column
    private Instant modified;

    @Column
    private Boolean completed;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserEntity userEntity;

}
