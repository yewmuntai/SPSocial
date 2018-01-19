package com.sp.social.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sp.social.model.Friendship;
import com.sp.social.model.Person;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
	@Query("select f from Friendship f where (f.person1 = :person1 and f.person2 = :person2) or (f.person1 = :person2 and f.person2 = :person1)")
	Optional<Friendship> findByPersons(@Param("person1") Person person1, @Param("person2") Person person2);
}
