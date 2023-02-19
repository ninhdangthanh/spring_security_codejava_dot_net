package net.codejava;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("")
	public String viewHomePage() {
		return "index";
	}
	
	@GetMapping("/admin/login")
	public String viewAdminLoginPage() {
		return "admin/admin_login";
	}
	
	@GetMapping("/admin/home")
	public String viewAdminHomePage() {
		return "admin/admin_home";
	}
	
	@GetMapping("/customer/login")
	public String viewCustomerLoginPage() {
		return "customer/customer_login";
	}
	
	@GetMapping("/customer/home")
	public String viewCustomerHomePage() {
		return "customer/customer_home";
	}	
}
