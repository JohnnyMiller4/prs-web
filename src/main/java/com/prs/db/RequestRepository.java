package com.prs.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;
import com.prs.business.User;
import com.prs.web.JsonResponse;

//T= type = Stuffy, ID = Integer
public interface RequestRepository extends CrudRepository<Request, Integer> {
	
	List<Request> findByUserId(int id);
	Request findById(int id);
	List<Request> findByUserNotAndStatus(User user, String status);

}
