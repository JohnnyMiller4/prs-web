package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.User;

//T= type = Stuffy, ID = Integer
public interface UserRepository extends CrudRepository<User, Integer> {

}
