package com.example.testcontainer.demo.config;

import com.aerospike.client.Host;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.config.AbstractAerospikeDataConfiguration;
import org.springframework.data.aerospike.repository.config.EnableAerospikeRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableTransactionManagement
@EnableAerospikeRepositories("com.example.testcontainer.demo")
public class AerospikeConfig extends AbstractAerospikeDataConfiguration {

    @Value("${aerospike.hosts}")
    public String aeroSpikesHosts;


    @Override
    protected Collection<Host> getHosts() {
        return
                Arrays.stream(aeroSpikesHosts.split(","))
                        .map(host -> host.split(":"))
                        .map(parts -> new Host(parts[0], Integer.parseInt(parts[1])))
                        .collect(Collectors.toList())

                ;
    }

    @Override
    protected String nameSpace() {
        return "myspace";
    }
}
