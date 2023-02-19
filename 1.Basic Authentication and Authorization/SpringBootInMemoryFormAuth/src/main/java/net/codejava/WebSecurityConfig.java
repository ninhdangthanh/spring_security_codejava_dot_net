package net.codejava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.activation.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		UserDetails user1 = User
				.withUsername("namhm")
				.password("$2a$10$JRma9uoX9DOwG/5jKyUPkeK9nJU2fA51hfu8mYXxcPRt5Cgj5zkXa")
				.roles("USER")
				.build();
		UserDetails user2 = User
				.withUsername("admin")
				.password("$2a$10$JRma9uoX9DOwG/5jKyUPkeK9nJU2fA51hfu8mYXxcPRt5Cgj5zkXa")
				.roles("ADMIN")
				.build();
		UserDetails user3 = User
				.withUsername("ninh")
				.password("$2a$10$JRma9uoX9DOwG/5jKyUPkeK9nJU2fA51hfu8mYXxcPRt5Cgj5zkXa")
				.roles("ADMIN")
				.build();
		
		return new InMemoryUserDetailsManager(user1, user2, user3);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/", "/home").permitAll()
			.mvcMatchers("/cpanel").hasRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("u").passwordParameter("p")
				.permitAll()
				.failureUrl("/loginerror")
				.defaultSuccessUrl("/loginsuccess")
				.and()
				.rememberMe()
				.tokenValiditySeconds(1 * 60 * 60) // expiration time: 1 HOUR
				.key("AbcdefghiJklmNoPqRstUvXyz")
			.and()
			.logout().permitAll()
			.logoutSuccessUrl("/logoutsuccess")
			.and()
			.exceptionHandling().accessDeniedPage("/403");
	}



}
