package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Request;
import com.prs.db.RequestRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/requests")
public class RequestController {

	@Autowired
	private RequestRepository reqRepo;
	
	//list - return all Requests
	@GetMapping("/") //exposes the following method to the web.
	public JsonResponse listRequests() {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(reqRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	
	//get - return 1 Request for the given id
	@GetMapping("/{id}") //exposes the following method to the web.
	public JsonResponse getRequest(@PathVariable int id) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(reqRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	//URL = http://localhost:8080/requests/5
	
	//add - adds a new Request
	@PostMapping("/")
	public JsonResponse addRequest(@RequestBody Request r) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(reqRepo.save(r));
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
		
	//update - updates a Request
	@PutMapping("/")
	public JsonResponse updateRequest(@RequestBody Request r) {
	JsonResponse jr = null;
		try {
			if (reqRepo.existsById(r.getId())) {
				jr = JsonResponse.getInstance(reqRepo.save(r));
			} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error updating Request. ID: " + r.getId() + " does not exist.");
				}
			}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
		
		//delete - delete a Request
		@DeleteMapping("/{id}")
		public JsonResponse deleteRequest(@PathVariable int id) {
		JsonResponse jr = null;	
			try {
				if (reqRepo.existsById(id)) {
					reqRepo.deleteById(id);
					jr = JsonResponse.getInstance("Delete sucessful!");
				} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Request. ID: " + id + " does not exist.");
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
