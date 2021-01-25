package com.taira.cntl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.taira.cntl.entity.User;
import com.taira.cntl.service.AuthenticateService;

@RestController
public class AuthenticateController {

	@Autowired
	private AuthenticateService authService;
	
	@GetMapping(value = "/", headers = "Accept=application/json")
	public String hello() {
		return authService.hello();
	}
	
	@GetMapping(value = "/secure/hello", headers = "Accept=application/json")
	public String helloSecure() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Gson gson = new GsonBuilder().create();
		User u = gson.fromJson(auth.getName(), new TypeToken<User>() {
		}.getType());
		
		return u.getEmail() + " helloSecure " + authService.hello();
	}
}
