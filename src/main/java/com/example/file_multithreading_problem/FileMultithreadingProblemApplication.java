package com.example.file_multithreading_problem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FileMultithreadingProblemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileMultithreadingProblemApplication.class, args);
	}

}
