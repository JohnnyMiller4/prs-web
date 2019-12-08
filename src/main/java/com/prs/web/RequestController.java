package com.prs.web;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Request;
import com.prs.business.User;
import com.prs.db.RequestRepository;
import com.prs.db.UserRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/requests")
public class RequestController {

	@Autowired
	private RequestRepository reqRepo;
	@Autowired
	private UserRepository userRepo;
	
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
			r.setStatus("New");
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
		
		@PutMapping("/submit-review")
		public JsonResponse submitReview(@RequestBody Request r) {
			JsonResponse jr = null;
				try {
					if (reqRepo.existsById(r.getId())) {
						if (r.getTotal() <= 50.00) {
							r.setStatus("Approved");
							} else {
								r.setStatus("Review");
							}
						r.setSubmittedDate(LocalDateTime.now());
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
		
		@PutMapping("/approve")
		public JsonResponse approveRequest(@RequestBody Request r) {
			JsonResponse jr = null;
				try {
					r.setStatus("Approved");
					r.setSubmittedDate(LocalDateTime.now());
					jr = JsonResponse.getInstance(reqRepo.save(r));
					}
				catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
			return jr;
			}
		
		@PutMapping("/reject")
		public JsonResponse rejectRequest(@RequestBody Request r) {
			JsonResponse jr = null;
				try {
					r.setStatus("Rejected");
					r.setSubmittedDate(LocalDateTime.now());
					jr = JsonResponse.getInstance(reqRepo.save(r));
					}
				catch (Exception e) {
					jr = JsonResponse.getInstance(e);
					e.printStackTrace();
				}
			return jr;
			}
		
		//list requests that are in review (that are not by the logged-in user)
		@GetMapping("/list-review/{id}") //http://localhost:8080/requests/list-review/
		public JsonResponse listRequest(@PathVariable int id) {
		JsonResponse jr = null;
			try {
				User user = userRepo.findById(id).get();
				jr = JsonResponse.getInstance(reqRepo.findByUserNotAndStatus(user, "Review"));
			}
			catch (Exception e) {
				jr = JsonResponse.getInstance(e);
				e.printStackTrace();
			}
		return jr;
		}
}
