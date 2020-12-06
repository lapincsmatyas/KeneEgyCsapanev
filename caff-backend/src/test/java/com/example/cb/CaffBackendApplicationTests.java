package com.example.cb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cb.controller.*;

@SpringBootTest
public class CaffBackendApplicationTests {
	
	@Autowired
	private AdminController adminController;
	@Autowired
	private AuthController authController;
	@Autowired
	private CAFFController caffController;
	@Autowired
	private UserController userController;

	@Test
	public void contextLoads() throws Exception {
		assertThat(adminController).isNotNull();
		assertThat(authController).isNotNull();
		assertThat(caffController).isNotNull();
		assertThat(userController).isNotNull();
	}

}
