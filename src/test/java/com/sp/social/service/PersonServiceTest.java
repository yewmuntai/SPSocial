package com.sp.social.service;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sp.social.exception.SPSocialException;
import com.sp.social.model.Person;
import com.sp.social.repository.PersonRepository;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PersonServiceTest {
	PersonService service;
	@Autowired
	PersonRepository personRepository;
	
	@Before
	public void init() {
//		PersonRepository pr = mock(PersonRepository.class);
		service = new PersonService();
		service.setService(personRepository);
	}
	
	@Test
	public void createPersonSuccess() {
		String email = "a@b.com";
		Person person = service.create(email);
		
		assertNotNull(person);
		assertEquals(person.getEmail(), email);
		assertTrue(person.getId() > 0);
	}

	@Test
	public void createPersonInvalidEmail() {
		String emails[] = {null, "", "ab.com", "@b.com", "a@", "a@b", "a@.com", "a@b."};
		for (int i=0; i<emails.length; i++) {
			String email = emails[i];
			try {
				service.create(email);
			}catch (SPSocialException e) {
				assertEquals(e.getMessage(), "Invalid Email");
			}
		}
	}
	
	@Test
	public void getExistingPerson() {
		String email = "a@b.com";
		Person created = service.create(email);

		Person retrieved = service.getPerson(email);
		
		assertEquals(created.getId(), retrieved.getId());
	}

	@Test
	public void getNewPerson() {
		String email = "a@b.com";
		
		Optional<Person> retrievedOp = personRepository.findByEmail(email);
		assertFalse(retrievedOp.isPresent());
		Person retrieved = service.getPerson(email);
		assertNotNull(retrieved);
		assertTrue(retrieved.getId() > 0);
	}

	@Test
	public void list() {
		Set<String> emails = new HashSet<>();
		emails.add("a1@b.com");
		emails.add("a2@b.com");
		emails.add("a3@b.com");
		List<Person> list = service.list();
		assertEquals(list.size(), 0);
		int i=1;
		for (Iterator<String> it=emails.iterator(); it.hasNext(); i++) {
			String email = it.next();
			service.create(email);
			list = service.list();
			assertEquals(list.size(), i);
			
			for (int x=0; x<list.size(); x++) {
				assertTrue(emails.contains(list.get(x).getEmail()));
			}
		}
	}
}
