package com.sp.social.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.exception.SPSocialException;
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
			System.out.println(email + " exists");
			person = personOp.get();
		}else {
			person = create(email);
			System.out.println(person.getId() + " created");
		}
		return person;
	}
	 
	public boolean makeFriend(String email1, String email2) {
		Person person1 = getPerson(email1);
		Person person2 = getPerson(email2);
		
		Optional<Friendship> friendshipOp1 = friendshipRepository.findByPerson1AndPerson2(person1, person2);

		Friendship f1 = null;
		Friendship f2 = null;

		if (friendshipOp1.isPresent()) {
			f1 = friendshipOp1.get();
			if (f1.isBlocked()) {
				// unblock
				f1.setBlocked(false);
			}
		}
		Optional<Friendship> friendshipOp2 = friendshipRepository.findByPerson1AndPerson2(person2, person1);
		if (friendshipOp2.isPresent()) {
			f2 = friendshipOp2.get();
			if (f2.isBlocked()) {
				throw new SPSocialException(person2.getEmail() + " has blocked " + person1.getEmail());
			}
		}

		if (f1 == null) {
			f1 = new Friendship();
			f1.setPerson1(person1);
			f1.setPerson2(person2);
		}
		if (f2 == null) {
			f2 = new Friendship();
			f2.setPerson1(person2);
			f2.setPerson2(person1);
		}
		
		f1 = friendshipRepository.save(f1);
		f2 = friendshipRepository.save(f2);

		return true;
	}
	
	public List<Person> list() {
		return personRepository.findAll();
	}
}
