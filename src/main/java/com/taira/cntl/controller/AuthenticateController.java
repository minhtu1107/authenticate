package com.taira.cntl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return "helloSecure" + authService.hello();
	}
}
