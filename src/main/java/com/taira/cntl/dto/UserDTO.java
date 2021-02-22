package com.taira.cntl.dto;

import java.io.Serializable;

import com.taira.cntl.entity.User;

import lombok.Data;

@Data
public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4464736339632508729L;

	private Integer id;
	
	private String email;
	
	private String role;
	
	public UserDTO() {
	}
	
	public UserDTO(User u) {
		this.id = u.getId();
		this.email = u.getEmail();
		this.role = "test";
	}
}
