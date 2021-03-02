package com.taira.cntl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taira.cntl.dto.UserDTO;
import com.taira.cntl.entity.User;
import com.taira.cntl.service.UserService;
import com.taira.cntl.util.StringUtil;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/", headers = "Accept=application/json")
	public String hello() {
		return userService.hello();
	}
	
	@GetMapping(value = "/secure/hello", headers = "Accept=application/json")
	public String helloSecure() {
		return " helloSecure " + userService.hello();
	}
	
	@GetMapping(value = "/hello", headers = "Accept=application/json")
	public String helloNonSecure() {
		return " hello " + userService.hello();
	}
	
	@GetMapping(value = "/getAllUsers", headers = "Accept=application/json")
	public Map<String, Object> getAllUsers(@RequestParam(defaultValue = "-1") Integer page) {
		return userService.getAllUsers(page);
	}
	
	@PutMapping(value = "/addUser", headers = "Accept=application/json")
	public UserDTO addUser(@RequestBody User u) {
		u.setPassword(StringUtil.getHashValue(u.getPassword(), "MD5"));
		return userService.addUser(u);
	}
	
	@DeleteMapping(value = "/removeUser", headers = "Accept=application/json")
	public ResponseEntity<Integer> removeUser(@RequestParam Integer id) {
		Integer errorCode = userService.removeUser(id);
		return ResponseEntity.ok().body(errorCode);
	}
	
	@PostMapping(value = "/secure/grantPermission", headers = "Accept=application/json")
	public ResponseEntity<Boolean> grantPermission(@RequestBody String[] email) {
		Boolean errorCode = userService.isAdmin(email[0]);
		return ResponseEntity.ok().body(errorCode);
	}
}
