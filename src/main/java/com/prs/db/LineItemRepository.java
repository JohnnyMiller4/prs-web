package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.LineItem;

//T= type = Stuffy, ID = Integer
public interface LineItemRepository extends CrudRepository<LineItem, Integer> {

}
