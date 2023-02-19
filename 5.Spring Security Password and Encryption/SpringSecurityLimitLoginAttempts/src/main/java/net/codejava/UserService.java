package net.codejava;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	public static final int MAX_FAILED_ATTEMPTS = 3;

	private static final long LOCK_TIME_DURATION = 1 * 60 * 60 * 1000; // 1 hours

	@Autowired
	private UserRepository userRepo;
	
	@Autowired RoleRepository roleRepo;
	
	@Autowired PasswordEncoder passwordEncoder;

	public void increaseFailedAttempts(User user) {
		int newFailAttempts = user.getFailedAttempt() + 1;
		userRepo.updateFailedAttempts(newFailAttempts, user.getEmail());
	}

	public void resetFailedAttempts(String email) {
		userRepo.updateFailedAttempts(0, email);
	}

	public void lock(User user) {
		user.setAccountNonLocked(false);
		user.setLockTime(new Date());

		userRepo.save(user);
	}

	public boolean unlockWhenTimeExpired(User user) {
		long lockTimeInMillis = user.getLockTime().getTime();
		long currentTimeInMillis = System.currentTimeMillis();

		if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
			user.setAccountNonLocked(true);
			user.setLockTime(null);
			user.setFailedAttempt(0);

			userRepo.save(user);

			return true;
		}

		return false;
	}
	
	public void registerDefaultUser(User user) {
		Role roleUser = roleRepo.findByName("User");
		user.addRole(roleUser);
		encodePassword(user);
		userRepo.save(user);
	}
	
	public List<User> listAll() {
		return userRepo.findAll();
	}

	public User get(Long id) {
		return userRepo.findById(id).get();
	}
	
	public List<Role> listRoles() {
		return roleRepo.findAll();
	}
	
	public void save(User user) {
		encodePassword(user);		
		userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);		
	}

	public void updateResetPasswordToken(String token, String email) throws Exception {
		User customer = userRepo.findByEmail(email);
		if (customer != null) {
			customer.setResetPasswordToken(token);
			userRepo.save(customer);
		} else {
			throw new Exception();
		}
	}

	public User getByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	public User getByResetPasswordToken(String token) {
		return userRepo.findByResetPasswordToken(token);
	}

	public void updatePassword(User user, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		user.setResetPasswordToken(null);
		userRepo.save(user);
	}
}
