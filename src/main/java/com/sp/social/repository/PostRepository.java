package com.sp.social.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sp.social.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
