/**
 * 
 */
package com.orbix.api;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Godfrey
 *
 */
@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {
	
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS =
	    {
	        "classpath:/META-INF/resources/",
			"classpath:/resources/",
	        "classpath:/static/", 
			"classpath:/public/",
			"classpath:/custom/",
			"file:/opt/myfiles/"
	    };
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/*").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	    
	}
}
