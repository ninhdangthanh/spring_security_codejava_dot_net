package net.codejava.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.codejava.CustomUserDetailsService;

@Configuration
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	} 

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll();
		
		http.antMatcher("/admin/**")
			.authorizeRequests().anyRequest().hasAuthority("ADMIN")
			.and()
			.formLogin()
				.loginPage("/admin/login")
				.usernameParameter("email")
				.loginProcessingUrl("/admin/login")
				.defaultSuccessUrl("/admin/home")
				.permitAll()
			.and()
			.logout()
				.logoutUrl("/admin/logout")
				.logoutSuccessUrl("/");	
	}	
    
    
}
