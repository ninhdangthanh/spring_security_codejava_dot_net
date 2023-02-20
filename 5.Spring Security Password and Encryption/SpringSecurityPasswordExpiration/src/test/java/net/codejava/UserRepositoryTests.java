package net.codejava;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Test
	public void testCreateUser() {
		User user = new User();
		user.setEmail("ravikumar@gmail.com");
		user.setPassword("$2a$10$g6sapEl2uri68YFU1Z.ek.aGzNXHCouc1QFw.LO6jxRh6k4OJhCUe");
		user.setFirstName("Ravi");
		user.setLastName("Kumar");
		
		User savedUser = userRepo.save(user);
		
		User existUser = entityManager.find(User.class, savedUser.getId());
		
		assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
		
	}
	
	@Test
	public void testAddRoleToNewUser() {
		Role roleAdmin = roleRepo.findByName("Admin");
		
		User user = new User();
		user.setEmail("ninh@gmail.com");
		user.setPassword("$2a$10$g6sapEl2uri68YFU1Z.ek.aGzNXHCouc1QFw.LO6jxRh6k4OJhCUe");
		user.setFirstName("Ninh");
		user.setPasswordChangedTime(new Date("2023/01/01"));
		user.setLastName("Dang");
		user.addRole(roleAdmin);		
		
		User savedUser = userRepo.save(user);
		
		assertThat(savedUser.getRoles().size()).isEqualTo(1);
	}
	
	@Test
	public void testAddRoleToExistingUser() {
		User user = userRepo.findById(3L).get();
		Role roleUser = roleRepo.findByName("User");
		Role roleCustomer = new Role(3);
		
		user.addRole(roleUser);
		user.addRole(roleCustomer);
		
		User savedUser = userRepo.save(user);
		
		assertThat(savedUser.getRoles().size()).isEqualTo(2);		
	}
	
	@Test
	public void testFindByEmail() {
		String email = "ravikumar@gmail.com";
		User user = userRepo.findByEmail(email);

		assertThat(user.getEmail()).isEqualTo(email);
	}
}
