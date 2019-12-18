package com.programmingonestop.healthchecker.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.programmingonestop.healthchecker.model.Gender;
import com.programmingonestop.healthchecker.model.HealthItem;
import com.programmingonestop.healthchecker.model.HealthSymptomSelector;
import com.programmingonestop.healthchecker.model.SelectorStatus;
import com.programmingonestop.healthchecker.service.LoadBodyLocationsService;

/*
 * This is a controller class for the SuperDoctor
 * This class loads information on body locations,sublocations and their symptoms
 */
@Controller
public class BodyInfo {
	@Autowired
	private LoadBodyLocationsService lbls;

	@GetMapping("/bodyinfo")
	public ModelAndView testing(ModelAndView mv) throws Exception {
		SelectorStatus selectorStatus = SelectorStatus.Boy;

		ArrayList<HealthItem> bodylocs = (ArrayList) lbls.loadbodyInfo(selectorStatus).get(0);

		Map<Integer, List<HealthItem>> bodysublocsmap = new HashMap<Integer, List<HealthItem>>();

		HashMap<Integer, HashMap<Integer, ArrayList<HealthSymptomSelector>>> sublocsymptomsmap = new HashMap<Integer, HashMap<Integer, ArrayList<HealthSymptomSelector>>>();

		ArrayList<HealthItem> bodysublocs = new ArrayList<HealthItem>();
		ArrayList<HealthSymptomSelector> sublocsymptoms = new ArrayList<HealthSymptomSelector>();
		HashMap<Integer, ArrayList<HealthSymptomSelector>> symptomselectorhash = new HashMap<Integer, ArrayList<HealthSymptomSelector>>();

		for (int i = 0; i < bodylocs.size(); i++) {

			bodysublocs = (ArrayList<HealthItem>) lbls.loadBodySubLocations(bodylocs.get(i).ID);// bodysublocs
			for (int j = 0; j < bodysublocs.size(); j++) {
				sublocsymptoms = (ArrayList<HealthSymptomSelector>) lbls.LoadSublocationSymptoms(bodysublocs.get(j).ID,
						selectorStatus);
				symptomselectorhash.put(j, sublocsymptoms);
				sublocsymptomsmap.put(i, symptomselectorhash);

			}

		}
		

		for (int i = 0; i < bodylocs.size(); i++) {
			List<HealthItem> items = lbls.loadBodySubLocations(bodylocs.get(i).ID);
			bodysublocsmap.put(i, items);

		}

		mv.setViewName("bodyinfo");

		mv.addObject("bodylocs", bodylocs);
		mv.addObject("bodysublocationsmap", bodysublocsmap);

		mv.addObject("sublocsymptomsmap", sublocsymptomsmap);
		return mv;
	}

	@GetMapping("/")
	public String index() {

		return "index";
	}

	@GetMapping("/userinfo")
	public ModelAndView userInfo(ModelAndView mv) {
		List<Integer> numberOfYears = new ArrayList<Integer>();
		for (int i = 0; i < 150; i++) {
			numberOfYears.add(i);
		}
		Calendar now = Calendar.getInstance();
		int beginyear = now.get(Calendar.YEAR);

		mv.setViewName("userinfo");
		mv.addObject("years", numberOfYears);
		mv.addObject("beginyear", beginyear);
		return mv;
	}

	@PostMapping("/userinfo")
	public String receiveUserInfo(@RequestParam Map<String, String> values) {
		Map uservalues = new HashMap<String, String>();
		uservalues = values;

		return "redirect:/bodyinfo";
	}

	@PostMapping("/diagonize")
	public ModelAndView diagonize(@RequestParam Map<String, String> values,ModelAndView mv) throws Exception {
		Map symptomIds = new HashMap<String, String>();
		symptomIds = values;
		List bodyinfoList = lbls.loadbodyInfo(SelectorStatus.Boy);
		HashMap<Integer,List<HealthSymptomSelector>> bodysymptomsList = (HashMap<Integer,List<HealthSymptomSelector>>) bodyinfoList.get(2);
		List<HealthSymptomSelector>symptoms=new ArrayList<HealthSymptomSelector>();
		List<HealthSymptomSelector>listsymptomsvaluesreceived=new ArrayList<HealthSymptomSelector>();

		
		for(int i=0;i<bodysymptomsList.size();i++) 
		{
			symptoms.addAll(bodysymptomsList.get(i));
		}
		
		
		for(int i=0;i<1000;i++) 
		{
			if((values.get(String.valueOf(i))!=null)) 
			{
				for(HealthItem bodyloc:lbls.loadBodyLocations()) 
				{
					if(bodyloc.ID==i) 
					{
						
						for(HealthItem ss:lbls.loadBodySubLocations(bodyloc.ID)) 
						{
							listsymptomsvaluesreceived.addAll(lbls.LoadSublocationSymptoms(ss.ID, SelectorStatus.Boy));
						}
					}
				}
			}
		}
		
		lbls.LoadDiagnosis(listsymptomsvaluesreceived, Gender.Male);
		
		mv.setViewName("diagonize");
		mv.addObject("values",listsymptomsvaluesreceived);
		
		return mv;
	}
}
