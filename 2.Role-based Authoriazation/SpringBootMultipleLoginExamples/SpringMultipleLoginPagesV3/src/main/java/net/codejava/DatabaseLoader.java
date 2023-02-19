package net.codejava;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.codejava.admin.User;
import net.codejava.admin.UserRepository;
import net.codejava.customer.Customer;
import net.codejava.customer.CustomerRepository;

@Configuration
public class DatabaseLoader {

	private UserRepository userRepo;
	private CustomerRepository customerRepo;

	public DatabaseLoader(UserRepository userRepo, CustomerRepository customerRepo) {
		this.userRepo = userRepo;
		this.customerRepo = customerRepo;
	}

	@Bean
	public CommandLineRunner initializeDatabase() {
		return args -> {
			User user1 = new User("david@gmail.com", "david123");
			User user2 = new User("john@yahoo.com", "john2020");
			
			userRepo.saveAll(List.of(user1, user2));
			
			Customer customer1 = new Customer("alex@gmail.com", "alex123", "Alex Stevenson");
			Customer customer2 = new Customer("peter@mail.ru", "peter246", "Peter Senkovski");
			
			customerRepo.saveAll(List.of(customer1, customer2));
			
			System.out.println("Database initialized");
		};
	}
}
