package com.taira.cntl.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "users", catalog = "cntl")
@Data
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7830157057217549427L;

	@Id
	@GeneratedValue
	@Column(name = "user_id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
}
