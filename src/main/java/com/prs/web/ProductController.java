package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Product;
import com.prs.db.ProductRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductRepository prodRepo;
	
	//list - return all Products
	@GetMapping("/") //exposes the following method to the web.
	public JsonResponse listProducts() {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prodRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	
	//get - return 1 Product for the given id
	@GetMapping("/{id}") //exposes the following method to the web.
	public JsonResponse getProduct(@PathVariable int id) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prodRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	//URL = http://localhost:8080/products/5
	
	//add - adds a new Product
	@PostMapping("/")
	public JsonResponse addProduct(@RequestBody Product u) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(prodRepo.save(u));
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
		
	//update - updates a Product
	@PutMapping("/")
	public JsonResponse updateProduct(@RequestBody Product p) {
	JsonResponse jr = null;
		try {
			if (prodRepo.existsById(p.getId())) {
				jr = JsonResponse.getInstance(prodRepo.save(p));
			} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error updating Product. ID: " + p.getId() + " does not exist.");
				}
			}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
		
		//delete - delete a Product
		@DeleteMapping("/{id}")
		public JsonResponse deleteProduct(@PathVariable int id) {
		JsonResponse jr = null;	
			try {
				if (prodRepo.existsById(id)) {
					prodRepo.deleteById(id);
					jr = JsonResponse.getInstance("Delete sucessful!");
				} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Product. ID: " + id + " does not exist.");
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
