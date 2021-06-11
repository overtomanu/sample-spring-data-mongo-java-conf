package com.luv2code.springdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.luv2code.springdemo.audit.AuditorAwareImpl;
import com.luv2code.springdemo.controller.ControllerScanMarker;
import com.luv2code.springdemo.conversationSupport.ConversationalSessionAttributeStore;

//Note - excluding subpackages with FilterType.ASPECTJ didnt work

@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses = {
		ControllerScanMarker.class })
public class WebConfig implements WebMvcConfigurer {

	// define a bean for ViewResolver

	@Bean
	public ViewResolver viewResolver() {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/");

		// https://www.webjars.org/documentation#springmvc
		// https://stackoverflow.com/questions/44200423/webjars-locator-doesnt-work-with-xml-based-spring-mvc-4-2-x-configuration
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("/webjars/").setCachePeriod(3600)
				.resourceChain(true); // !!! very important
	}

	@Bean
	public SessionAttributeStore conversationalSessionAttributeStore() {
		return new ConversationalSessionAttributeStore();
	}

	/*
	@Bean
	AuditorAware<User> auditorProvider(UserService userService) {
		return () -> Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.filter(Authentication::isAuthenticated)
				.map(Authentication::getPrincipal)
				.map((Object principalObj) -> (
						((UserAwareUserDetails) principalObj).getUser()));
	}
	*/

	@Bean
	AuditorAware<String> auditorProvider() { return new AuditorAwareImpl(); }
}
