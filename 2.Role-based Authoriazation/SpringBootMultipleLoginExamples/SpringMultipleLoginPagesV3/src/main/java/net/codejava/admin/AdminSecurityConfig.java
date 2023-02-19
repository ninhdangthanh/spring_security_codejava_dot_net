package net.codejava.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class AdminSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider1() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider1());

		http.authorizeRequests().antMatchers("/").permitAll();

		http.antMatcher("/admin/**")
			.authorizeRequests().anyRequest().authenticated()
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

		return http.build();
	}

}
