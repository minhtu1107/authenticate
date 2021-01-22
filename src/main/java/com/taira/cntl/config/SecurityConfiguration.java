package com.taira.cntl.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserDetailsService userDetailsService;
	
    @Autowired
    private CustomApiAuthenticationProvider customApiAuth;
	 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customApiAuth);
    }
    
	@Bean
	public PasswordEncoder encoder() {
		PasswordEncoder passwordEncoder = new PasswordEncoder() {
			
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return getHashValue(rawPassword.toString(), "MD5").equals(encodedPassword);
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
				return getHashValue(rawPassword.toString(), "MD5");
			}
		};
		
		return passwordEncoder;
	}

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/free/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
	public String getHashValue(String content, String instanceType) {
		MessageDigest md;
		byte[] mdBytes;
		try {
			md = MessageDigest.getInstance(instanceType);
			md.update(content.getBytes());
			mdBytes = md.digest();
			String password = DatatypeConverter.printHexBinary(mdBytes);
			return password;
		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}
}
