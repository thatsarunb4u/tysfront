package com.digipasar.tyfrontend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digipasar.tyfrontend.entity.Space;

@Service
public class SpaceService {
	
	@Autowired
	private SpaceRepository repository;
	
	public List<Space> getAllSpace(){
		Iterable<Space> spaceList = repository.findAll();
		List<Space> resultList = new ArrayList<Space>();
		if(spaceList != null) {
			for(Space space:spaceList) {
				resultList.add(space);
				System.out.println(space);
			}
		}
		
		return resultList;
	}

}
