package com.sp.social.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.model.Friendship;
import com.sp.social.repository.FriendshipRepository;

@Service
public class FriendshipService {
	@Autowired
	private FriendshipRepository friendshipRepository;
	
	public List<Friendship> list() {
		return friendshipRepository.findAll();
	}
}
