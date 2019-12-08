package com.river.bpmDemo;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class BpmDemoApplication {

	@Autowired
	private RuntimeService runtimeService;

	public static void main(String... args) {
		SpringApplication.run(BpmDemoApplication.class, args);
	}
}
