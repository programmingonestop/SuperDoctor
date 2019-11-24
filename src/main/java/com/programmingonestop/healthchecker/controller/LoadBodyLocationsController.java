package com.programmingonestop.healthchecker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.programmingonestop.healthchecker.model.HealthItem;
import com.programmingonestop.healthchecker.model.HealthSymptomSelector;
import com.programmingonestop.healthchecker.service.LoadBodyLocationsService;

@Controller
public class LoadBodyLocationsController {
	@Autowired
	private LoadBodyLocationsService lbls;
	/*
	 * @PostMapping("/body-locations") public List<HealthItem> loadBodyLocations()
	 * throws Exception { return lbls.loadBodyLocations(); }
	 */
	
	@GetMapping("/testing")
	public String testing(@RequestParam Map values) 
	{
		System.out.println(values);
		return "testing";
	}

	@GetMapping("/body-sublocations/{locId}")
	public ModelAndView loadBodySubLocations(@PathVariable int locId,ModelAndView mv) throws Exception {
		
		List<HealthItem>healthItems=lbls.loadBodySubLocations(locId);
		mv.setViewName("body-sublocations");
		mv.addObject("bodySubLocs", healthItems);
		return mv;
		
		
	}

	@GetMapping("/body-sublocation-symptom/{subLocId}")
	public ModelAndView LoadSublocationSymptoms(@PathVariable int subLocId, ModelAndView mv) throws Exception 
	{
		List<HealthSymptomSelector>symptoms=lbls.LoadSublocationSymptoms(subLocId);
		mv.setViewName("sublocation-symptoms");
		mv.addObject("symptoms", symptoms);
		return mv;
	}

	@GetMapping("/diagonize/{selectedSublocationID}")
	public List<Integer> diagonize(@RequestBody List<HealthSymptomSelector> selectedSymptoms,
			@PathVariable int selectedSublocationID) throws Exception {
		return lbls.diagonize(selectedSymptoms, selectedSublocationID);
	}

	@GetMapping("/")
	public String index() {
		return "index";

	}

	@GetMapping("/body-locations")
	public ModelAndView symptoms(ModelAndView mv) throws Exception {
		mv.setViewName("body-locations");
		
		List<HealthItem>bodylocs=lbls.loadBodyLocations();
		
		mv.addObject("bodylocs",bodylocs);
		return mv;
	}
	

	
}
