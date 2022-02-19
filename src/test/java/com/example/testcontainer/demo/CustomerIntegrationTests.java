package com.example.testcontainer.demo;

import com.example.testcontainer.demo.models.CustomerModelAS;
import com.example.testcontainer.demo.repo.CustomerAerospikeRepo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Slf4j
public class CustomerIntegrationTests {

    public static final String AEROSPIKE_NAMESPACE = "myspace";

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    CustomerAerospikeRepo customerAerospikeRepo;

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:13.3")
            .withDatabaseName("mydb")
            .withUsername("root")
            .withPassword("pass");

    @Container
    public static GenericContainer aerospikeContainer = new GenericContainer("aerospike")
            .withExposedPorts(3000, 3001, 3002)
            .withEnv("NAMESPACE", AEROSPIKE_NAMESPACE)
            .withEnv("FEATURE_KEY_FILE", "/etc/aerospike/features.conf")
            .withClasspathResourceMapping("trial-features.conf",
                    "/etc/aerospike/features.conf",
                    BindMode.READ_ONLY)
            .waitingFor(Wait.forLogMessage(".*migrations: complete.*", 1));


    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);


        registry.add("aerospike.hosts", () -> {
                    String ip = aerospikeContainer.getHost();
                    return String.join(",", Arrays.asList(
                            getIp(ip, aerospikeContainer.getMappedPort(3000)),
                            getIp(ip, aerospikeContainer.getMappedPort(3001)),
                            getIp(ip, aerospikeContainer.getMappedPort(3002))
                    ));
                }
        );

    }

    @BeforeAll
    public static void init() {
        System.out.println("LOGS ****************************");
        System.out.println(aerospikeContainer.getLogs());
        System.out.println("END ****************************");
    }


    @Test
    void when_using_a_clean_db_this_should_be_empty() {
        List<Customer> customers = customerDao.findAll();
        Assertions.assertThat(customers).hasSize(6);
    }

    @Test
    public void aerospikeRepoTest() {
        CustomerModelAS customerModelAS = new CustomerModelAS();
        customerModelAS.setId(1L);
        customerModelAS.setFirstName("first name 1 ");
        customerModelAS.setLastName("last name 1 ");

        customerAerospikeRepo.save(customerModelAS);

        for (CustomerModelAS as : customerAerospikeRepo.findAll()) {
            log.info("data in aero spike {} ", as);
        }
    }


    private static String getIp(String ip, Integer port) {
        String res = ip + ":" + port;
        log.info("aerospike ip {}", res);
        return res;
    }
}
