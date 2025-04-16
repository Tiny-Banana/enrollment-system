package com.powerpuffgirls.uploadgradeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "com.powerpuffgirls.common.model",
        "com.powerpuffgirls.uploadgradeservice.model"
})
public class UploadGradeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadGradeServiceApplication.class, args);
    }

}
