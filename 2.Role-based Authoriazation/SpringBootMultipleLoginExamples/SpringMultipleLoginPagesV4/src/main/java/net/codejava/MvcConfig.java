package net.codejava;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("").setViewName("index");
		registry.addViewController("/admin/login").setViewName("admin/admin_login");
		registry.addViewController("/admin/home").setViewName("admin/admin_home");
		registry.addViewController("/customer/login").setViewName("customer/customer_login");
		registry.addViewController("/customer/home").setViewName("customer/customer_home");
	}

}
