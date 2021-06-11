package com.luv2code.springdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.luv2code.springdemo.ComponentScanMarker;
import com.luv2code.springdemo.conversationSupport.ConversationalSessionAttributeStore;
import com.luv2code.springdemo.conversationSupport.SessionConversationAspect;
import com.luv2code.springdemo.repository.RepositoryPackageMarker;

@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableMongoRepositories(basePackageClasses = { RepositoryPackageMarker.class })
@EnableMongoAuditing
@ComponentScan(basePackageClasses = {
		ComponentScanMarker.class }, excludeFilters = {
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.luv2code\\.springdemo\\.config\\.*"),
				@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.luv2code\\.springdemo\\.controller\\.*"),
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConversationalSessionAttributeStore.class) })
@Import({ MongoDBConfig.class })

public class RootConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers("/customer/showForm*").hasAnyRole("MANAGER", "ADMIN")
			.antMatchers("/customer/save*").hasAnyRole("MANAGER", "ADMIN")
			.antMatchers("/customer/delete").hasRole("ADMIN")
			.antMatchers("/customer/**").hasRole("EMPLOYEE")
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/webjars/**").permitAll()
			.and()
			.formLogin()
				.loginPage("/showMyLoginPage")
				.loginProcessingUrl("/authenticateTheUser")
				.permitAll()
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/access-denied");
		
	}
	
	@Bean
	public SessionConversationAspect sessionConversationAspect() {
		return new SessionConversationAspect();
	}

	// uncomment below code if you want to overwrite bean definition registry
	// for requestDataValueProcessorPostProcessor
	/*
	
	@Bean("requestDataValueProcessor")
	public ConversationIdRequestProcessor conversationIdRequestProcessor() {
		return new ConversationIdRequestProcessor();
	}
	
	@Bean
	public static BeanDefinitionRegistryPostProcessor requestDataValueProcessorPostProcessor() {
	return new BeanDefinitionRegistryPostProcessor() {
	
		@Override
		public void postProcessBeanFactory(
				ConfigurableListableBeanFactory beanFactory)
				throws BeansException {}
	
		@Override
		public void postProcessBeanDefinitionRegistry(
				BeanDefinitionRegistry registry) throws BeansException {
			registry.removeBeanDefinition("requestDataValueProcessor");
			registry.registerBeanDefinition("requestDataValueProcessor",
					new RootBeanDefinition(
							ConversationIdRequestProcessor.class));
		}
	
	};
	}
	*/
}






