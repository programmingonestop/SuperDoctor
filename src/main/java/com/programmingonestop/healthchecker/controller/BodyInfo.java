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

import sun.awt.SunHints.Value;


/*
 * This is a controller class for the SuperDoctor
 * This class loads information on body locations,sublocations and their symptoms
 */
@Controller
public class BodyInfo {
	@Autowired
	private LoadBodyLocationsService lbls;
	@GetMapping("/bodyinfo")
	public ModelAndView testing(ModelAndView mv) throws Exception 
	{
		SelectorStatus selectorStatus=SelectorStatus.Boy;
		
		ArrayList<HealthItem> bodylocs=(ArrayList) lbls.loadbodyInfo(selectorStatus).get(0);
		ArrayList<HealthItem> bodySublocations=(ArrayList) lbls.loadbodyInfo(selectorStatus).get(1);
		ArrayList<HealthSymptomSelector> symptoms=(ArrayList) lbls.loadbodyInfo(selectorStatus).get(2);
		
		
		for(HealthItem item:bodylocs) 
		{
			bodySublocations.addAll(lbls.loadBodySubLocations(item.ID));
			
		}
		
		
		Map<Integer,List<HealthItem>> bodysublocsmap=new HashMap<Integer, List<HealthItem>>();
		
		for(int i=0;i<bodylocs.size();i++) 
		{
			List<HealthItem> items=lbls.loadBodySubLocations(bodylocs.get(i).ID);
			bodysublocsmap.put(i, items);
		
		}
		System.out.println(bodysublocsmap.get(1).get(0).Name+"YSLEEEEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSEEEEEEEEEEEEE");
		
		
		for(HealthItem subloc:bodySublocations) 
		{
			symptoms=(ArrayList<HealthSymptomSelector>) lbls.LoadSublocationSymptoms(subloc.ID,selectorStatus);
			
		}
		mv.setViewName("bodyinfo");
		mv.addObject("bodylocs",bodylocs);
		mv.addObject("locsublocs", bodySublocations);
		mv.addObject("sublocsymptoms", symptoms);
		return mv;
	}
	@GetMapping("/")
	public String index() 
	{
		return "index";
	}
	
	@GetMapping("/userinfo")
	public ModelAndView userInfo(ModelAndView mv) 
	{
		List<Integer>numberOfYears=new ArrayList<Integer>();
		for(int i=0;i<150;i++) 
		{
			numberOfYears.add(i);
		}
		Calendar now =Calendar.getInstance();
		int beginyear=now.get(Calendar.YEAR);
		
		
		
		mv.setViewName("userinfo");
		mv.addObject("years",numberOfYears);
		mv.addObject("beginyear", beginyear);
		return mv;
	}
	@PostMapping("/userinfo")
	public String receiveUserInfo(@RequestParam Map<String,String> values) 
	{
		Map uservalues=new HashMap<String, String>();
		uservalues=values;
		return "redirect:/bodyinfo";
	}
	
	@PostMapping("/diagonize")
	public String diagonize(@RequestParam Map<String,String> values) throws Exception 
	{
		Map symptomIds=new HashMap<String, String>();
		symptomIds=values;
		List bodyinfoList=lbls.loadbodyInfo(SelectorStatus.Boy);
		List<HealthSymptomSelector>bodysymptomsList=(List<HealthSymptomSelector>) bodyinfoList.get(2);
		ArrayList selectedSymptoms=new ArrayList();
		
		
		
		

		for(int i=0;i<1000;i++) 
		{
			if(values.get(String.valueOf(i))!=null) 
			{
				for(HealthSymptomSelector item:bodysymptomsList) 
				{
					if(item.ID==i) 
					{
						selectedSymptoms.add(item);
					}
				}
			}
		}
		List<Integer> diagnosis=lbls.diagonize(selectedSymptoms, Gender.Male, SelectorStatus.Boy);
		System.out.println(lbls.diagonize(selectedSymptoms, Gender.Male, SelectorStatus.Boy)+"DDDDDDDDDDDDDIAAAAAAAAAAGGGGGGGGGGNNNNNNNNSOOOOOOSISSSSSSSSSSS");
		return "diagonize";
	}
}
