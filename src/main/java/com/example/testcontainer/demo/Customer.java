package com.example.testcontainer.demo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Data
@Slf4j
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public long id;

    @Column
    public String firstName;

    @Column
    public String lastName;
}