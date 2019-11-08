package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;

//T= type = Stuffy, ID = Integer
public interface RequestRepository extends CrudRepository<Request, Integer> {

}
