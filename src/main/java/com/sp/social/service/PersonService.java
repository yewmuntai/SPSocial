package com.sp.social.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.model.Person;
import com.sp.social.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;
	
	public Person create(String email) {
		Person person = new Person();
		person.setEmail(email);
		
		return personRepository.save(person);
	}
}
