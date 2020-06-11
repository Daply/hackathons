package com.luckydrive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.luckydrive.model.Point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class LuckyDriveApplication extends SpringBootServletInitializer{

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}

	public static void main(String[] args) {
		// SpringApplication.run(LuckyDriveApplication.class, args);
		configureApplication(new SpringApplicationBuilder()).run(args);
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		return builder.sources(LuckyDriveApplication.class);
	}

}
