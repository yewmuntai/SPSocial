package com.sp.social.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sp.social.assembler.FriendsAssembler;
import com.sp.social.assembler.PostAssembler;
import com.sp.social.data.FriendsData;
import com.sp.social.data.PersonListData;
import com.sp.social.data.PostData;
import com.sp.social.data.PostResultData;
import com.sp.social.model.Friendship;
import com.sp.social.model.Person;
import com.sp.social.service.FriendshipService;
import com.sp.social.service.PersonService;
import com.sp.social.service.PostService;

@RestController
@RequestMapping(value="/api/person")
public class PersonController {
	@Autowired
	private PersonService personService;
	@Autowired
	private FriendshipService friendshipService;
	@Autowired
	private PostService postService;
	
	@GetMapping
	public ResponseEntity<PersonListData> list() {
		List<Person> list = personService.list();
		PersonListData data = new PersonListData();
		data.setSuccess(true);
		data.setList(list);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@GetMapping(value="/{email}/friends")
	public ResponseEntity<FriendsData> getFriends(@PathVariable(value="email")String email) {
		List<Friendship> friendships = friendshipService.findFriends(email);
		FriendsData resp = FriendsAssembler.toFriendsData(friendships);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
	@GetMapping(value="/{email}/common")
	public ResponseEntity<FriendsData> getCommonFriends(@PathVariable(value="email")String email, @RequestParam(value="friendEmail")String friendEmail) {
		List<Friendship> friendships = friendshipService.findCommonFriends(email, friendEmail);
		FriendsData resp = FriendsAssembler.toFriendsData(friendships);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}
	
	@PostMapping(value="/post")
	public ResponseEntity<PostResultData> post(@RequestBody PostData data) {
		List<String> receiverEmails = postService.post(data.getSender(), data.getText());
		PostResultData resp = PostAssembler.toPostResultData(receiverEmails);
		return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
	}
}
