package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Vendor;
import com.prs.db.VendorRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/vendors")
public class VendorController {

	@Autowired
	private VendorRepository venRepo;
	
	//list - return all Vendors
	@GetMapping("/") //exposes the following method to the web.
	public JsonResponse listVendors() {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(venRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	
	//get - return 1 Vendor for the given id
	@GetMapping("/{id}") //exposes the following method to the web.
	public JsonResponse getVendor(@PathVariable int id) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(venRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	//URL = http://localhost:8080/Vendors/5
	
	//add - adds a new Vendor
	@PostMapping("/")
	public JsonResponse addVendor(@RequestBody Vendor v) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(venRepo.save(v));
		}
		catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.getStackTrace();
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
		
	//update - updates a Vendor
	@PutMapping("/")
	public JsonResponse updateVendor(@RequestBody Vendor v) {
	JsonResponse jr = null;
		try {
			if (venRepo.existsById(v.getId())) {
				jr = JsonResponse.getInstance(venRepo.save(v));
			} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error updating Vendor. ID: " + v.getId() + " does not exist.");
				}
			}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
		
		//delete - delete a Vendor
		@DeleteMapping("/{id}")
		public JsonResponse deleteVendor(@PathVariable int id) {
		JsonResponse jr = null;	
			try {
				if (venRepo.existsById(id)) {
					venRepo.deleteById(id);
					jr = JsonResponse.getInstance("Delete sucessful!");
				} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Vendor. ID: " + id + " does not exist.");
				}
			}
			catch (DataIntegrityViolationException dive) {
				jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
				dive.getStackTrace();
			}
			catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.getStackTrace();
			}
		return jr;
		}
}
