package org.kerrin.dungeon.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class DispatcherConfig {
	private static final Logger logger = LoggerFactory.getLogger(DispatcherConfig.class);
	
	@Bean
	public InternalResourceViewResolver configureInternalResourceViewResolver()
	{
		logger.info("Configuring view resolver");
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}
}
