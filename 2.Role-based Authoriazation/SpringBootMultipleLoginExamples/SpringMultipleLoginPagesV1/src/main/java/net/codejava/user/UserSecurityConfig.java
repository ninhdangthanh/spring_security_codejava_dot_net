package net.codejava.user;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(2)
public class UserSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.antMatcher("/user/**")
			.authorizeRequests().anyRequest().hasAuthority("USER")
			.and()
			.formLogin()
				.loginPage("/user/login")
				.usernameParameter("email")
				.loginProcessingUrl("/user/login")
				.defaultSuccessUrl("/user/home")
				.permitAll()
			.and()
			.logout()
				.logoutUrl("/user/logout")
				.logoutSuccessUrl("/");		
	}	
}
