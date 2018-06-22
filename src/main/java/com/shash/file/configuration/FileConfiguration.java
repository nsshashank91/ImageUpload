package com.shash.file.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.shash.file.model.ImageModel;

@Configuration
@ComponentScan(basePackages={"com.shash.file.configuration","com.shash.file.controller","com.shash.file.service"})
@EntityScan("com.shash.file.model")
@EnableJpaRepositories(basePackages={"com.shash.file.repository"})
public class FileConfiguration {
	
	@Bean
	@Scope("prototype")
	public ImageModel getImageModel(){
		return new ImageModel();
	}
}
