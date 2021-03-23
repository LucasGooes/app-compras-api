package br.com.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.cursomc.services.DBService;
import br.com.cursomc.services.EmailService;
import br.com.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean intantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();	
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}

}
