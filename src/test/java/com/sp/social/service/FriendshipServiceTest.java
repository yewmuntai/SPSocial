package com.sp.social.service;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sp.social.exception.SPSocialException;
import com.sp.social.model.Friendship;
import com.sp.social.model.Person;
import com.sp.social.repository.FriendshipRepository;
import com.sp.social.repository.PersonRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FriendshipServiceTest {
	FriendshipService service;
	PersonService personService;
	@Autowired
	FriendshipRepository friendshipRepository;
	@Autowired
	PersonRepository personRepository;

	@Before
	public void init() {
		service = new FriendshipService();
		personService = new PersonService();
		personService.setService(personRepository);
		service.setService(friendshipRepository, personRepository, personService);
	}
	
	@Test
	public void createFriendshipSuccess() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		boolean result = service.makeFriend(email1, email2);
		
		assertTrue(result);
		
		String[][] data = {{email1, email2}, {email2, email1}};
		for (int i=0; i<data.length; i++) {
			Optional<Person> p1Op = personRepository.findByEmail(data[i][0]);
			assertTrue(p1Op.isPresent());
			Optional<Person> p2Op = personRepository.findByEmail(data[i][1]);
			assertTrue(p2Op.isPresent());
			
			Person person1 = p1Op.get();
			Person person2 = p2Op.get();
			
			Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
			assertTrue(friendshipOp.isPresent());
			Friendship friendship = friendshipOp.get();
			assertEquals(friendship.getPerson1(), person1);
			assertEquals(friendship.getPerson2(), person2);
			assertFalse(friendship.isFollowUpdates());
			assertFalse(friendship.isBlocked());
		}
	}
	
	@Test
	public void findFriends() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		String email3 = "c@social.com";
		
		List<Friendship> list = service.findFriends(email1);
		assertEquals(list.size(), 0);

		service.makeFriend(email1, email2);
		list = service.findFriends(email1);
		assertEquals(list.size(), 1);
		
		service.makeFriend(email3, email1);
		list = service.findFriends(email1);
		assertEquals(list.size(), 2);
	}
	
	@Test
	public void findCommonFriends() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		String email3 = "c@social.com";

		service.makeFriend(email1, email2);
		service.makeFriend(email3, email1);
		
		List<Friendship> list = service.findCommonFriends(email2, email3);
		assertEquals(list.size(), 1);
		assertEquals(list.get(0).getPerson2().getEmail(), email1);
		
	}
	
	@Test 
	public void newSubscribe() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		Person person1 = personService.create(email1);
		Person person2 = personService.create(email2);

		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertFalse(friendshipOp.isPresent());
		
		service.subscribe(email1, email2);
		friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		Friendship friendship = friendshipOp.get();
		assertEquals(friendship.getPerson1(), person1);
		assertEquals(friendship.getPerson2(), person2);
		assertTrue(friendship.isFollowUpdates());
		assertFalse(friendship.isBlocked());
	}

	@Test 
	public void cuurentSubscribe() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		service.makeFriend(email1, email2);

		Person person1 = personService.getPerson(email1);
		Person person2 = personService.getPerson(email2);
		
		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		
		service.subscribe(email1, email2);
		friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		Friendship friendship = friendshipOp.get();
		assertEquals(friendship.getPerson1(), person1);
		assertEquals(friendship.getPerson2(), person2);
		assertTrue(friendship.isFollowUpdates());
		assertFalse(friendship.isBlocked());
	}
	
	@Test 
	public void newBlock() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		Person person1 = personService.create(email1);
		Person person2 = personService.create(email2);

		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertFalse(friendshipOp.isPresent());
		
		service.block(email1, email2);
		friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		Friendship friendship = friendshipOp.get();
		assertEquals(friendship.getPerson1(), person1);
		assertEquals(friendship.getPerson2(), person2);
		assertFalse(friendship.isFollowUpdates());
		assertTrue(friendship.isBlocked());
	}

	@Test 
	public void cuurentBlock() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		service.makeFriend(email1, email2);

		Person person1 = personService.getPerson(email1);
		Person person2 = personService.getPerson(email2);
		
		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		assertFalse(friendshipOp.get().isBlocked());
		
		service.block(email1, email2);
		friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		Friendship friendship = friendshipOp.get();
		assertEquals(friendship.getPerson1(), person1);
		assertEquals(friendship.getPerson2(), person2);
		assertFalse(friendship.isFollowUpdates());
		assertFalse(friendship.isBlocked());
	}
	
	@Test
	public void addPreviouslyBlocked() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		Person person1 = personService.getPerson(email1);
		Person person2 = personService.getPerson(email2);
		
		service.block(email1, email2);

		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertTrue(friendshipOp.isPresent());
		Friendship friendship = friendshipOp.get();
		assertTrue(friendship.isBlocked());
		assertEquals(friendship.getPerson1(), person1);
		assertEquals(friendship.getPerson2(), person2);
		
		service.makeFriend(email1, email2);
		friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		friendship = friendshipOp.get();
		assertFalse(friendship.isBlocked());
		assertEquals(friendship.getPerson1(), person1);
		assertEquals(friendship.getPerson2(), person2);
	}

	@Test
	public void addBlockedPerson() {
		String email1 = "a@social.com";
		String email2 = "b@social.com";
		
		Person person1 = personService.getPerson(email1);
		Person person2 = personService.getPerson(email2);
		
		service.block(email2, email1);

		Optional<Friendship> friendshipOp = friendshipRepository.findByPerson1AndPerson2(person1, person2);
		assertFalse(friendshipOp.isPresent());
		try {
			service.makeFriend(email1, email2);
		}catch (SPSocialException e) {
			assertEquals(e.getMessage(), email2 + " has blocked " + email1);
		}
	}
}
