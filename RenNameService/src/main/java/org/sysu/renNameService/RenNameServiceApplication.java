package org.sysu.renNameService;

/**
 * Author: Gordan
 * Date  : 2018/1/19
 * Usage : Ren Name Service Entry Point.
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.sysu.renNameService.repository")
@EntityScan(basePackages = "org.sysu.renNameService.entity")
public class RenNameServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenNameServiceApplication.class, args);
    }

}

