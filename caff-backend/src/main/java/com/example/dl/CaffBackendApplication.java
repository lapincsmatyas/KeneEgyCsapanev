package com.example.dl;

import com.example.dl.repository.RoleRepository;
import com.example.dl.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackageClasses ={ UserRepository.class, RoleRepository.class})
public class CaffBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaffBackendApplication.class, args);
	}

}
