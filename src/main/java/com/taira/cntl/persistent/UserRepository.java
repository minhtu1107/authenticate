package com.taira.cntl.persistent;

import org.springframework.data.repository.CrudRepository;

import com.taira.cntl.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
