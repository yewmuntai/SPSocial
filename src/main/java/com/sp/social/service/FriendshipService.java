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
}
