package com.sp.social.service;

import java.util.ArrayList;
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
		if (requestorEmail != null && requestorEmail.length() > 0) {
			Optional<Person> requestorOp = personRepository.findByEmail(requestorEmail);
			if (requestorOp.isPresent()) {
				if (targetEmail != null && targetEmail.length() > 0) {
					Optional<Person> targetOp = personRepository.findByEmail(targetEmail);
					if (targetOp.isPresent()) {
						Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(requestorOp.get(), targetOp.get());
						if (friendshipOp.isPresent()) {
							Friendship fs = friendshipOp.get();
							fs.setFollowUpdates(true);
							fs.setBlocked(false);
							friendshipRepository.save(fs);
							return true;
						}
						throw new SPSocialException("Requestor and target are not friends");
					}
				}
				throw new SPSocialException("Invalid target email");
			}
		}
		throw new SPSocialException("Invalid requestor email");
	}

	public boolean block(String requestorEmail, String targetEmail) {
		if (requestorEmail != null && requestorEmail.length() > 0) {
			Person requestor = personService.getPerson(requestorEmail);
			if (targetEmail != null && targetEmail.length() > 0) {
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
					return true;
				}else {
					Friendship fs = new Friendship();
					fs.setPerson1(requestor);
					fs.setPerson2(target);
					fs.setBlocked(true);
					friendshipRepository.save(fs);
				}
			}
			throw new SPSocialException("Invalid target email");
		}
		throw new SPSocialException("Invalid requestor email");
	}
}
