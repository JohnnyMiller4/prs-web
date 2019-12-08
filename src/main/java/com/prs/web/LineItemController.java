package com.prs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineItemRepository;
import com.prs.db.RequestRepository;
import com.prs.web.JsonResponse;

@CrossOrigin
@RestController
@RequestMapping("/line-items")
public class LineItemController {

	@Autowired
	private LineItemRepository lnRepo;
	@Autowired
	private RequestRepository reqRepo;
	
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
	//URL = http://localhost:8080/line-items/5
	
	//add - adds a new LineItem
	@PostMapping("/")
	public JsonResponse addLineItem(@RequestBody LineItem ln) {
	JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lnRepo.save(ln));
			//call recalculate
			recalculateTotal(ln.getRequest());
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
				//call recalculate
				recalculateTotal(ln.getRequest());
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
					Request lineItem = lnRepo.findById(id).get().getRequest();
					lnRepo.deleteById(id);
					jr = JsonResponse.getInstance("Delete successful!");
					//call recalculate
					recalculateTotal(lineItem);
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

		
		private void recalculateTotal(Request r) {
			//get list of a LineItem
			List<LineItem> lines = lnRepo.findAllByRequestId(r.getId());
			//loop through list to sum a total
			double total = 0.0;
			for (LineItem line: lines) {
				total += line.getTotal();
			}
			//save that total in the Request instance
			r.setTotal(total);
			try {
				reqRepo.save(r);
			} catch (Exception e) {
				throw e;
			}
		}
		
		
		@GetMapping("/lines-for-pr/{id}") //http://localhost:8080/line-items/lines-for-pr/1
		public JsonResponse listRequest(@PathVariable int id) {
			JsonResponse jr = null;
			try {
				jr = JsonResponse.getInstance(lnRepo.findByRequestId(id));
			}
			catch (Exception e) {
				jr = JsonResponse.getInstance(e.getMessage());
				e.printStackTrace();
			}
		return jr;
		}
}
