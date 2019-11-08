package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.User;
import com.prs.db.UserRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	//list - return all Users
	@GetMapping("/") //exposes the following method to the web.
	public JsonResponse listUsers() {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	
	//get - return 1 User for the given id
	@GetMapping("/{id}") //exposes the following method to the web.
	public JsonResponse getUser(@PathVariable int id) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	//URL = http://localhost:8080/users/5
	
	//add - adds a new User
	@PostMapping("/")
	public JsonResponse addUser(@RequestBody User u) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(userRepo.save(u));
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
		
	//update - updates a User
	@PutMapping("/")
	public JsonResponse updateUser(@RequestBody User u) {
	JsonResponse jr = null;
		try {
			if (userRepo.existsById(u.getId())) {
				jr = JsonResponse.getInstance(userRepo.save(u));
			} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error updating User. ID: " + u.getId() + " does not exist.");
				}
			}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
		
		//delete - delete a User
		@DeleteMapping("/{id}")
		public JsonResponse deleteUser(@PathVariable int id) {
		JsonResponse jr = null;	
			try {
				if (userRepo.existsById(id)) {
					userRepo.deleteById(id);
					jr = JsonResponse.getInstance("Delete sucessful!");
				} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error deleting User. ID: " + id + " does not exist.");
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
