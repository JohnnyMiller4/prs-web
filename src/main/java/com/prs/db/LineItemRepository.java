package com.prs.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.LineItem;
import com.prs.business.Request;

//T= type = Stuffy, ID = Integer
public interface LineItemRepository extends CrudRepository<LineItem, Integer> {
	
	List<LineItem> findByRequestId(int id);
	List<LineItem> findAllByRequestId(int id);

}
