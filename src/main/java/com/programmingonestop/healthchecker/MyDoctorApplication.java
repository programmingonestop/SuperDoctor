package com.programmingonestop.healthchecker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyDoctorApplication implements CommandLineRunner
{
	
	

	public static void main(String[] args) {
		SpringApplication.run(MyDoctorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception 
	{
		new DiagnosisClientUtil();
	}

}

