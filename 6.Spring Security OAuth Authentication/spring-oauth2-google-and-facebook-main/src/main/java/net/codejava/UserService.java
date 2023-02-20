package net.codejava;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;


	public void processOAuthPostLogin(String email, String oauth2ClientName) {
		User existUser = repo.getUserByEmail(email);
		
		if (existUser == null) {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setProvider(Provider.valueOf(oauth2ClientName.toUpperCase()));
			newUser.setEnabled(true);			
			
			repo.save(newUser);
			
			System.out.println("Created new user: " + email);
		}
		
	}
	
}
