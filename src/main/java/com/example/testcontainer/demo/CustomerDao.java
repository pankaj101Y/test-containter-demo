package com.example.testcontainer.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerDao {

    @Autowired
    CustomerRepo customerRepo;



    public List<Customer> findAll() {
        return customerRepo.findAll();
    }
}
