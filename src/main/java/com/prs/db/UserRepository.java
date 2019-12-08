package com.prs.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.User;

//T= type = Stuffy, ID = Integer
public interface UserRepository extends CrudRepository<User, Integer> {
	
	User findByUsernameAndPassword(String username, String password);

}
