package com.taira.cntl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taira.cntl.entity.User;
import com.taira.cntl.persistent.UserRepository;

@Component
public class AuthenticateService {

	@Autowired
	private UserRepository userRepo;
	
	public String hello() {
		String test = "hello hihi";
		try {
			User u = userRepo.findById(1).get();
			if(u!=null) {
				test = u.getEmail();
			}
		} catch (Exception e) {
			
		}
		return test;
	}
}
