package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.LineItem;
import com.prs.db.LineItemRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/lineitems")
public class LineItemController {

	@Autowired
	private LineItemRepository lnRepo;
	
	//list - return all LineItems
	@GetMapping("/") //exposes the following method to the web.
	public JsonResponse listLineItems() {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lnRepo.findAll());
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	
	//get - return 1 LineItem for the given id
	@GetMapping("/{id}") //exposes the following method to the web.
	public JsonResponse getLineItem(@PathVariable int id) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lnRepo.findById(id));
		}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
	//URL = http://localhost:8080/lineitems/5
	
	//add - adds a new LineItem
	@PostMapping("/")
	public JsonResponse addLineItem(@RequestBody LineItem ln) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lnRepo.save(ln));
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
		
	//update - updates a LineItem
	@PutMapping("/")
	public JsonResponse updateLineItem(@RequestBody LineItem ln) {
	JsonResponse jr = null;
		try {
			if (lnRepo.existsById(ln.getId())) {
				jr = JsonResponse.getInstance(lnRepo.save(ln));
			} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error updating LineItem. ID: " + ln.getId() + " does not exist.");
				}
			}
		catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
	return jr;
	}
		
		//delete - delete a LineItem
		@DeleteMapping("/{id}")
		public JsonResponse deleteLineItem(@PathVariable int id) {
		JsonResponse jr = null;	
			try {
				if (lnRepo.existsById(id)) {
					lnRepo.deleteById(id);
					jr = JsonResponse.getInstance("Delete sucessful!");
				} else {
				//record doesn't exist
				jr = JsonResponse.getInstance("Error deleting LineItem. ID: " + id + " does not exist.");
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
