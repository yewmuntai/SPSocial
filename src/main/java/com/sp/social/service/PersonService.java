package com.sp.social.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.model.Friendship;
import com.sp.social.model.Person;
import com.sp.social.repository.FriendshipRepository;
import com.sp.social.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private FriendshipRepository friendshipRepository;
	
	public Person create(String email) {
		Person person = new Person();
		person.setEmail(email);
		
		return personRepository.save(person);
	}
	
	public Person getPerson(String email) {
		Optional<Person> personOp = personRepository.findByEmail(email);
		Person person = null;
		if (personOp.isPresent()) {
			System.out.println(email + " exists");
			person = personOp.get();
		}else {
			person = new Person();
			person.setEmail(email);
			person = personRepository.save(person);
			System.out.println(person.getId() + " created");
		}
		return person;
	}
	 
	public boolean makeFriend(String email1, String email2) {
		boolean result = false;
		
		Person person1 = getPerson(email1);
		Person person2 = getPerson(email2);
		
		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		if (!friendshipOp.isPresent()) {
			Friendship f1 = new Friendship();
			f1.setPerson1(person1);
			f1.setPerson2(person2);
			f1 = friendshipRepository.save(f1);

			Friendship f2 = new Friendship();
			f2.setPerson1(person2);
			f2.setPerson2(person1);
			f2 = friendshipRepository.save(f2);

			result = true;
		}
		
		return result;
	}
	
	public List<Person> list() {
		return personRepository.findAll();
	}
}
