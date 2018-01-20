package com.sp.social.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.model.Friendship;
import com.sp.social.model.Person;
import com.sp.social.repository.FriendshipRepository;
import com.sp.social.repository.PersonRepository;

@Service
public class FriendshipService {
	@Autowired
	private FriendshipRepository friendshipRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PersonService personService;
	
	public List<Friendship> list() {
		return friendshipRepository.findAll();
	}
	
	public List<Friendship> findFriends(String email) {
		if (email != null && email.length() > 0) {
			Optional<Person> personOp = personRepository.findByEmail(email);
			if (personOp.isPresent()) {
				return friendshipRepository.findByPerson1(personOp.get());
			}
		}
		return new ArrayList<>();
	}
	
	public List<Friendship> findCommonFriends(String email1, String email2) {
		if (email1 != null && email1.length() > 0) {
			Optional<Person> personOp1 = personRepository.findByEmail(email1);
			if (personOp1.isPresent()) {
				if (email2 != null && email2.length() > 0) {
					Optional<Person> personOp2 = personRepository.findByEmail(email2);
					if (personOp2.isPresent()) {
						return friendshipRepository.findCommonFriends(personOp1.get(), personOp2.get());
					}
				}
				return new ArrayList<>();
			}
		}
		return new ArrayList<>();
	}
	
	public boolean subscribe(String requestorEmail, String targetEmail) {
		Person requestor = personService.getPerson(requestorEmail);
		Person target = personService.getPerson(targetEmail);
		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(requestor, target);
		Friendship fs = null; 
		if (friendshipOp.isPresent()) {
			fs = friendshipOp.get();
		}else {
			fs = new Friendship();
			fs.setPerson1(requestor);
			fs.setPerson2(target);
		}
		fs.setFollowUpdates(true);
		fs.setBlocked(false);
		friendshipRepository.save(fs);
		return true;
	}

	public boolean block(String requestorEmail, String targetEmail) {
		Person requestor = personService.getPerson(requestorEmail);
		Person target = personService.getPerson(targetEmail);
		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(requestor, target);
		if (friendshipOp.isPresent()) {
			Friendship fs = friendshipOp.get();
			fs.setFollowUpdates(false);
			fs.setBlocked(true);
			friendshipRepository.save(fs);

			Optional<Friendship> friendshipOp2 = friendshipRepository.findByPerson1AndPerson2(target, requestor);
			if (friendshipOp2.isPresent()) {
				friendshipRepository.delete(friendshipOp2.get());
			}
		}else {
			Friendship fs = new Friendship();
			fs.setPerson1(requestor);
			fs.setPerson2(target);
			fs.setBlocked(true);
			friendshipRepository.save(fs);
		}
		return true;
	}
}
