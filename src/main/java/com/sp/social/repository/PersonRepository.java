package com.sp.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.social.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	public Person findByEmail(String email);
}
