package com.prs.db;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Vendor;

//T= type = Stuffy, ID = Integer
public interface VendorRepository extends CrudRepository<Vendor, Integer> {

}
