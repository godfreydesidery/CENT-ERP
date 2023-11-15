package com.orbix.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication()
@ComponentScan(basePackages={"com.orbix.api"})
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableSwagger2
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Data
@RequiredArgsConstructor
public class MainApplication {
	protected ConfigurableApplicationContext springContext;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Don't do this in production, use a proper list  of allowed origins
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@PostConstruct
	  public void setUp() {
	    objectMapper.registerModule(new JavaTimeModule());
	  }
	
	public static void main(String[] args) throws Throwable {
		SpringApplication.run(MainApplication.class, args);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public Docket erpApi() {
	  return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.orbix.api")).build();
    }
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipart = new CommonsMultipartResolver();
	    multipart.setMaxUploadSize(3 * 1024 * 1024);
	    return multipart;
	}

	@Bean
	@Order(0)
	public MultipartFilter multipartFilter() {
	    MultipartFilter multipartFilter = new MultipartFilter();
	    multipartFilter.setMultipartResolverBeanName("multipartResolver");
	    return multipartFilter;
	}	
}
