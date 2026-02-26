package com.brayden.firstrestapibooks.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="book")
@Table(name="book") // you don't need to specify this if entity and table names are the same
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // used UUID because id is a String could use Auto or Identity to auto increment
    private String id;
    @Column(name="name") // can skip this if name here matches exactly name in db so this is not needed
    private String name;
    @Column(name="author")
    private String author;
    @Column(name="price")
    private String price;
}
