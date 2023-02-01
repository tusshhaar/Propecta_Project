package com.masai.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.masai.model.DTO;
import com.masai.model.MyData;
import com.masai.model.MyEntry;

@RestController
public class EntryController {
	

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/entries/{category}")
	public List<DTO> getEntriesHandler(@PathVariable("category") String category) {

		MyData d = restTemplate.getForObject("https://api.publicapis.org/entries", MyData.class);

		List<MyEntry> entries = d.getEntries();

		List<DTO> list = new ArrayList<>();
		
		for(MyEntry en:entries) {
			
			if(en.getCategory().equals(category))
				list.add(new DTO(en.getApi(), en.getDescription()));
		}

		
		return list;

	}
	
	public String saveEntry(MyEntry entry){
		
		MyData d = restTemplate.getForObject("https://api.publicapis.org/entries", MyData.class);
		
		List<MyEntry> entries = d.getEntries();
		
		for(MyEntry en:entries) {
			
			if(en.getCategory().equals(entry.getCategory())) {
				
				return "Entry Already Exists";
				
			}
		}
		
		restTemplate.postForEntity("https://api.publicapis.org/entries", entry, ResponseEntity.class);
		
		return "Saved Successfully";
		
		
	}


}
