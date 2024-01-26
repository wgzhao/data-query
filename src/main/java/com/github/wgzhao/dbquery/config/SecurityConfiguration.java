package com.github.wgzhao.dbquery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/admin/**").authenticated()
						.anyRequest().permitAll()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.defaultSuccessUrl("/admin", true)
						.permitAll()
				)
				.logout(logout -> logout
						.deleteCookies("JSESSIONID")
						.permitAll());
		// @formatter:on
		return http.build();
	}

	@Bean
	UserDetailsManager users(DataSource dataSource) {
		return  new JdbcUserDetailsManager(dataSource);
	}
}
