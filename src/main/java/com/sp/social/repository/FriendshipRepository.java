package com.sp.social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.social.model.Friendship;
import com.sp.social.model.Person;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	Optional<Friendship> findByPerson1AndPerson2(Person person1, Person person2);
	List<Friendship> findByPerson1(Person person);
}
