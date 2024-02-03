package com.curso.ecommerce.service;




import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity {

	// @Autowired
	// private UserDetailsService userDetailsService;

	/*
	 * @SuppressWarnings("removal")
	 * 
	 * @Bean public AuthenticationManager authManager(HttpSecurity
	 * http,UserDetailsService userDetailsService, PasswordEncoder passwordEncoder)
	 * throws Exception { return
	 * http.getSharedObject(AuthenticationManagerBuilder.class)
	 * .userDetailsService(userDetailsService) .passwordEncoder(getEnecoder())
	 * .and() .build();
	 * 
	 * }
	 */
	/*
	 * @Bean public AuthenticationManager authenticationManager( UserDetailsService
	 * userDetailsService, BCryptPasswordEncoder passwordEncoder) {
	 * DaoAuthenticationProvider authenticationProvider = new
	 * DaoAuthenticationProvider();
	 * authenticationProvider.setUserDetailsService(userDetailsService);
	 * authenticationProvider.setPasswordEncoder(passwordEncoder);
	 * 
	 * return new ProviderManager(authenticationProvider); }
	 * 
	 */

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//		http.csrf(csrf -> csrf.disable())
				
//				.authorizeHttpRequests(authRequest -> {
					//authRequest.requestMatchers("/").permitAll();
					//authRequest.requestMatchers("/administrador/**").hasRole("ADMIN");
					//authRequest.requestMatchers("/productos/**").hasRole("ADMIN");
					//authRequest.requestMatchers("/usuario/**").permitAll();
					//authRequest.anyRequest().authenticated();
//						})

			//	.formLogin((formLogin) -> formLogin.usernameParameter("mail").passwordParameter("password")
			//			.loginPage("/usuario/login").defaultSuccessUrl("/usuario/acceder"));
		;
		http
		.authorizeHttpRequests(authConfig -> {
			//authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
			//authConfig.requestMatchers( "/usuario/**").permitAll();
			
			//authConfig.requestMatchers( "/css/**","/vendor/**").permitAll();
			authConfig.requestMatchers( "/administrador/**").hasRole("ADMIN");
			authConfig.requestMatchers("/productos/**").hasRole("ADMIN");
			authConfig.anyRequest().permitAll();
		})
		.csrf(csrf -> csrf.disable())
		.userDetailsService(myUserDetailsService())
		.formLogin((formLogin) -> formLogin.usernameParameter("username").passwordParameter("password")
		.loginPage("/usuario/login").permitAll()
		
		.defaultSuccessUrl("/usuario/acceder"));
		

//		.formLogin(withDefaults()) // Login with browser and Build in Form
//		.httpBasic(withDefaults()); // Login with Insomnia or Postman and Basic Auth
	return http.build();
		

	}

	@Bean
	UserDetailsService myUserDetailsService() {
		return new UserDetailServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ApplicationListener<AuthenticationSuccessEvent> successEvent() {
		return event -> {
			System.err.println("Success Login " + event.getAuthentication().getClass().getName() + " - "
					+ event.getAuthentication().getName());
		};
	}

	@Bean
	public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent() {
		return event -> {
			System.err.println("Bad Credentials Login " + event.getAuthentication().getClass().getName() + " - "
					+ event.getAuthentication().getName());
		};
	}
}
