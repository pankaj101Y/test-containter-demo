package com.example.testcontainer.demo.repo;

import com.example.testcontainer.demo.models.CustomerModelAS;
import org.springframework.data.aerospike.repository.AerospikeRepository;

public interface  CustomerAerospikeRepo extends AerospikeRepository<CustomerModelAS,Long> {
}
