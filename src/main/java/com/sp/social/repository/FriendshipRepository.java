package com.sp.social.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sp.social.model.Friendship;
import com.sp.social.model.Person;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	Optional<Friendship> findByPerson1AndPerson2(Person person1, Person person2);
	List<Friendship> findByPerson1(Person person);
	
	@Query("SELECT f FROM Friendship f WHERE person1 = :person1 AND person2 IN (SELECT person2 FROM Friendship f2 WHERE person1 = :person2)")
	List<Friendship> findCommonFriends(@Param("person1")Person person1, @Param("person2")Person person2);

	@Query("SELECT f FROM Friendship f WHERE person2 = :person AND blocked=false")
	List<Friendship> findByFollowers(@Param("person")Person person);
}
