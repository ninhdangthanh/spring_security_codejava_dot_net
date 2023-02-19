package net.codejava;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired RoleRepository roleRepo;
	
	@Autowired PasswordEncoder passwordEncoder;

	@Autowired
	private MailService mailService;

	public void generateOneTimePassword(User user)
			throws UnsupportedEncodingException, MessagingException {
		String OTP = RandomString.make(8);
		String encodedOTP = passwordEncoder.encode(OTP);

		user.setOneTimePassword(encodedOTP);
		user.setOtpRequestedTime(new Date());

		userRepo.save(user);

		sendOTPEmail(user, OTP);
	}

	public void sendOTPEmail(User user, String OTP)
			throws MessagingException, UnsupportedEncodingException {

		String content = "<p>Hello " + user.getFirstName() + "</p>"
				+ "<p>For security reason, you're required to use the following "
				+ "One Time Password to login:</p>"
				+ "<p><b>" + OTP + "</b></p>"
				+ "<br>"
				+ "<p>Note: this OTP is set to expire in 5 minutes.</p>";

		String to = user.getEmail();
		String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";
		mailService.sendEmail(to, subject, content);
	}

	public void clearOTP(User user) {
		user.setOneTimePassword(null);
		user.setOtpRequestedTime(null);
		userRepo.save(user);
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

	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}
}
