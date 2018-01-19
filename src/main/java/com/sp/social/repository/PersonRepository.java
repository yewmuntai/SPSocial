package com.sp.social.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.social.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
	public Optional<Person> findByEmail(String email);
}
