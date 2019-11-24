package com.programmingonestop.healthchecker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.programmingonestop.healthchecker.DiagnosisClientUtil;
import com.programmingonestop.healthchecker.diagnosis.client.DiagnosisClient;
import com.programmingonestop.healthchecker.model.DiagnosedSpecialisation;
import com.programmingonestop.healthchecker.model.Gender;
import com.programmingonestop.healthchecker.model.HealthDiagnosis;
import com.programmingonestop.healthchecker.model.HealthIssueInfo;
import com.programmingonestop.healthchecker.model.HealthItem;
import com.programmingonestop.healthchecker.model.HealthSymptomSelector;
import com.programmingonestop.healthchecker.model.SelectorStatus;

@Service
public class LoadBodyLocationsService {
	
	private static DiagnosisClient _diagnosisClient;

	public List<HealthItem> loadBodyLocations() throws Exception {

		_diagnosisClient=DiagnosisClientUtil._diagnosisClient;
		List<HealthItem> bodyLocations = _diagnosisClient.loadBodyLocations();

		if (bodyLocations == null || bodyLocations.size() == 0)
			throw new Exception("Empty body locations results");

		for (HealthItem loc : bodyLocations)
			System.out.println(loc.Name + " (" + loc.ID + ")");

		return bodyLocations;
	}
	

	public List<HealthItem> loadBodySubLocations(int locId) throws Exception {
		List<HealthItem> bodySublocations = _diagnosisClient.loadBodySubLocations(locId);
        
		return bodySublocations;
	}
	
	
	public  List<HealthSymptomSelector> LoadSublocationSymptoms(int subLocId) throws Exception
	{
	    List<HealthSymptomSelector> symptoms = _diagnosisClient.loadSublocationSymptoms(subLocId, SelectorStatus.Man);

	    return symptoms;
	}
	
	
	public List<Integer> diagonize(List<HealthSymptomSelector>selectedSymptoms,int selectedSublocationID) throws Exception 
	{
		// Load diagnosis (reloading if data is not conclusive)
	    int count = 0;
	    int maxTries = 10;
	    boolean sucess = false;
	    List<Integer> diagnosis = new ArrayList<Integer>();
	    while(sucess!=true) {
	        try {
	        	diagnosis = LoadDiagnosis(selectedSymptoms);
	        	sucess= true;
	        } catch (Exception diagnosisException){
	           	// reload data if diagnosis result is not conclusive
	        	
	        	selectedSymptoms = LoadSublocationSymptoms(selectedSublocationID);
	        	if (++count == maxTries) throw diagnosisException;
	        sucess=false;
	        }
	}	
	    return diagnosis;
}
	
	
	
	public List<Integer> LoadDiagnosis(List<HealthSymptomSelector> selectedSymptoms) throws Exception
	{
	
		List<Integer> selectedSymptomsIds = new ArrayList<Integer>();
		for(HealthSymptomSelector symptom : selectedSymptoms){
			selectedSymptomsIds.add(symptom.ID);
		}
		
	    List<HealthDiagnosis> diagnosis = _diagnosisClient.loadDiagnosis(selectedSymptomsIds, Gender.Male, 1988);

	    if (diagnosis == null || diagnosis.size() == 0)
	    {
	    	System.out.println("No diagnosis results for symptom " + selectedSymptoms.get(0).Name);
	        return null;
	    }

	    for (HealthDiagnosis d : diagnosis){
	    	String specialistions = "";
	    	for(DiagnosedSpecialisation spec : d.Specialisation)
	    		specialistions = specialistions.concat(spec.Name + ", ");
	        System.out.println(d.Issue.Name + " - " + d.Issue.Accuracy + "% \nSpecialisations : " + specialistions);
	    }

	    List<Integer> retValue = new ArrayList<Integer>();
	    for(HealthDiagnosis diagnose : diagnosis)
	    	retValue.add(diagnose.Issue.ID);
	    return retValue;
	}

	
	
	
	public void LoadSpecialisations(List<HealthSymptomSelector> selectedSymptoms) throws Exception
	{
		

		List<Integer> selectedSymptomsIds = new ArrayList<Integer>();
		for(HealthSymptomSelector symptom : selectedSymptoms){
			selectedSymptomsIds.add(symptom.ID);
		}
		
	    List<DiagnosedSpecialisation> specialisations = _diagnosisClient.loadSpecialisations(selectedSymptomsIds, Gender.Male, 1988);

	    if (specialisations == null || specialisations.size() == 0)
	    {
	        System.out.println("No specialisations for symptom " + selectedSymptoms.get(0).Name);
	        return;
	    }

	    for (DiagnosedSpecialisation s : specialisations)
	        System.out.println(s.Name + " - " + s.Accuracy + "%");
	}
	
	
	
	
	static void LoadProposedSymptoms(List<HealthSymptomSelector> selectedSymptoms) throws Exception
	{
		List<Integer> selectedSymptomsIds = new ArrayList<Integer>();
		for(HealthSymptomSelector symptom : selectedSymptoms){
			selectedSymptomsIds.add(symptom.ID);
		}
	    List<HealthItem> proposedSymptoms = _diagnosisClient.loadProposedSymptoms(selectedSymptomsIds, Gender.Male, 1988);

	    if (proposedSymptoms == null || proposedSymptoms.size() == 0)
	    {
	    	System.out.println("No proposed symptoms for selected symptom " + selectedSymptoms.get(0).Name);
	        return;
	    }

	    String proposed = "";
	    for(HealthItem diagnose : proposedSymptoms)
	    	proposed = proposed.concat(diagnose.Name) + ", ";
	    
	    System.out.println("Proposed symptoms: " + proposed);
	}

	
	public void LoadIssueInfo(int issueId) throws Exception
	{
	    HealthIssueInfo issueInfo = _diagnosisClient.loadIssueInfo(issueId);
	    System.out.println("Issue info");
	    System.out.println("Name: " + issueInfo.Name);
	    System.out.println("Professional Name: " +issueInfo.ProfName );
	    System.out.println("Synonyms: " + issueInfo.Synonyms);
	    System.out.println("Short Description: " + issueInfo.DescriptionShort );
	    System.out.println("Description: " + issueInfo.Description);
	    System.out.println("Medical Condition: " + issueInfo.MedicalCondition);
	    System.out.println("Treatment Description: " +issueInfo.TreatmentDescription );
	    System.out.println("Possible symptoms: " + issueInfo.PossibleSymptoms + "\n");
	    
	}
		
}
