package com.example.testcontainer.demo.models;


import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class CustomerModelAS {

    @Id
    public long id;

    public String firstName;

    public String lastName;
}
