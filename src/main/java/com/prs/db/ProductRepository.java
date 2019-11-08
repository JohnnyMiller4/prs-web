package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Product;

//T= type = Stuffy, ID = Integer
public interface ProductRepository extends CrudRepository<Product, Integer> {

}
