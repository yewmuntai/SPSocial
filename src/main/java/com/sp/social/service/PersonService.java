package com.sp.social.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.exception.SPSocialException;
import com.sp.social.model.Person;
import com.sp.social.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;
	
	void setService(PersonRepository pr) {
		if (personRepository == null) {
			personRepository = pr;
		}
	}
	
	public Person create(String email) {
		Person person = new Person();
		
		if (!simpleEmailCheck(email)) {
			throw new SPSocialException("Invalid Email");
		}
		
		person.setEmail(email);
		
		return personRepository.save(person);
	}
	
	private boolean simpleEmailCheck(String email) {
		System.out.println("checking email");
		if (email == null || email.length() == 0) {
			return false;
		}
		
		int idx1 = email.indexOf('@');
		if (idx1 < 1) {
			return false;
		}
		
		int idx2 = email.indexOf('.', idx1);
		if (idx2 - idx1 < 2 || idx2+1 >= email.length()) {
			return false;
		}
		
		return true;
	}
	
	public Person getPerson(String email) {
		Optional<Person> personOp = personRepository.findByEmail(email);
		Person person = null;
		if (personOp.isPresent()) {
			person = personOp.get();
		}else {
			person = create(email);
		}
		return person;
	}
	 
	public List<Person> list() {
		return personRepository.findAll();
	}
}
