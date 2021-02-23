package com.taira.cntl.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taira.cntl.dto.UserDTO;
import com.taira.cntl.entity.User;
import com.taira.cntl.persistent.UserRepository;

@Component
public class UserService {

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
	
	public Map<String, Object> getAllUsers(Integer page) {
		return userRepo.getAllUser(page);
	}
	
	public UserDTO addUser(User u) {
		try {
			User newUser = userRepo.save(u);
			return new UserDTO(newUser);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Integer removeUser(Integer id) {
		try {
			userRepo.deleteById(id);
			return 200;
		} catch (Exception e) {
			return 500;
		}
	}
}
