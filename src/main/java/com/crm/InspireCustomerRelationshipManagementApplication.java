package com.crm;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class InspireCustomerRelationshipManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(InspireCustomerRelationshipManagementApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper()
	{
	    return new ModelMapper();
	}

}
