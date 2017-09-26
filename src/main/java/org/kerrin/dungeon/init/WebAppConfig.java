package org.kerrin.dungeon.init;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;
import org.kerrin.dungeon.task.ScoreProcessorTask;
import org.kerrin.dungeon.utils.Facebook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.mail.javamail.JavaMailSender; 
import org.springframework.mail.javamail.JavaMailSenderImpl; 

@Configuration
@EnableWebMvc
@ComponentScan("org.kerrin.dungeon")
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories("org.kerrin.dungeon.repository")
@EnableScheduling
public class WebAppConfig extends WebMvcConfigurerAdapter {
 
    private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
    private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";
 
    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
    private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
    
    private static final String HOST_URL = "host.url";
    private static final String LOCAL_MODE = "localMode";
    
    private static final String FACEBOOK_API_ID = "facebook.app.id";
    private static final String FACEBOOK_API_SECRET = "facebook.app.secret";
    
    private static final String SPRING_MAIL_AUTH = "spring.mail.smtp.auth";
    private static final String SPRING_MAIL_SMTP_STARTTLS = "spring.mail.smtp.starttls";
    private static final String SPRING_MAIL_HOST = "spring.mail.host";
    private static final String SPRING_MAIL_PORT = "spring.mail.port";
    private static final String SPRING_MAIL_PROTOCOL = "spring.mail.protocol";
    private static final String SPRING_MAIL_USERNAME = "spring.mail.username";
    private static final String SPRING_MAIL_PASSWORD = "spring.mail.password";
 
    @Resource
    private Environment env;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/resources/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/WEB-INF/resources/images/");
        registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/resources/js/");
        registry.addResourceHandler("/audio/**").addResourceLocations("/WEB-INF/resources/audio/");
        registry.addResourceHandler("/assets/**").addResourceLocations("/WEB-INF/resources/assets/");
    }

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
		rb.setBasenames(new String[] { "messages", "validation" });
		return rb;
	}
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
 
        dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));
        //System.out.println("---------------------------------"+env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
        //System.out.println("---------------------------------"+env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
 
        return dataSource;
    }
 
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
         
        entityManagerFactoryBean.setJpaProperties(hibProperties());
         
        return entityManagerFactoryBean;
    }
 
    private Properties hibProperties() {
        Properties properties = new Properties();
        properties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
        properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
        properties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
        properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));
        return properties;
    }
    
    @Bean
    public Facebook facebook() {
    	Facebook facebook = new Facebook(env.getRequiredProperty(HOST_URL),
    			env.getRequiredProperty(FACEBOOK_API_ID), 
    			env.getRequiredProperty(FACEBOOK_API_SECRET));
    	return facebook;
    }
 
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
    
    @Bean 
    public TaskExecutor taskExecuter() {
    	ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    	taskExecutor.setCorePoolSize(5);
    	taskExecutor.setMaxPoolSize(10);
    	taskExecutor.setQueueCapacity(10);
    	taskExecutor.initialize();
    	return taskExecutor;
    }
    
    @Bean
    public ScoreProcessorTask scoreProcessorTask() {
    	return new ScoreProcessorTask();
    }
    
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", env.getProperty(SPRING_MAIL_AUTH));
        mailProperties.put("mail.smtp.starttls.enable", env.getProperty(SPRING_MAIL_SMTP_STARTTLS));
        mailProperties.put("mail.smtp.starttls.required", env.getProperty(SPRING_MAIL_SMTP_STARTTLS));
        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setHost(env.getProperty(SPRING_MAIL_HOST));
        mailSender.setPort(Integer.parseInt(env.getProperty(SPRING_MAIL_PORT)));
        mailSender.setProtocol(env.getProperty(SPRING_MAIL_PROTOCOL));
        mailSender.setUsername(env.getProperty(SPRING_MAIL_USERNAME));
        mailSender.setPassword(env.getProperty(SPRING_MAIL_PASSWORD));
        return mailSender;
    }
    
    @Bean
    public String hostUrl() { 
    	return env.getRequiredProperty(HOST_URL);
    }
    
    @Bean
    public boolean localMode() { 
    	return env.getRequiredProperty(LOCAL_MODE).equalsIgnoreCase("true");
    }
}
