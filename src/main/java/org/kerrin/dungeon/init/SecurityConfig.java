package org.kerrin.dungeon.init;

import org.kerrin.dungeon.service.impl.UserDetailsServiceImpl;
import org.kerrin.dungeon.utils.ShaPasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	protected void configure(AuthenticationManagerBuilder registry) throws Exception {
		registry.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());		
	}

    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder();
    }
    
    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
    	webSecurity
            .ignoring()
                // All of Spring Security will ignore the requests
                .antMatchers("/api/**") // APIs use a key
                .antMatchers("/createAccount");
    }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.debug("Initialised spring security");
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/**").permitAll() // APIs use a key. Note: this doesn't work!
			.antMatchers(HttpMethod.GET, "/api/**").permitAll() // APIs use a key
			.antMatchers("/admin/**").hasAnyRole("View","Modify","Delete")
			.antMatchers("/play/**").authenticated()
			.and()
			  .formLogin().loginPage("/login").failureUrl("/?error=Login%20Failed")
			  .usernameParameter("username").passwordParameter("password")
			.and()
			  .logout().logoutSuccessUrl("/?message=Logged%20Out")
			.and()
			  .exceptionHandling().accessDeniedPage("/login/403")
            .and()
			  .csrf();
	}
}
