package com.sp.social.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sp.social.assembler.FriendsAssembler;
import com.sp.social.data.FriendshipListData;
import com.sp.social.data.MakeFriendData;
import com.sp.social.data.RequestData;
import com.sp.social.data.ResponseData;
import com.sp.social.model.Friendship;
import com.sp.social.service.FriendshipService;

@RestController
@RequestMapping(value="/api/friend")
public class FriendController {
	@Autowired
	private FriendshipService friendshipService;
	
	@PostMapping
	public ResponseEntity<ResponseData> makeFriend(@RequestBody MakeFriendData data) {
		String[] emails = data.getFriends();
		boolean result = friendshipService.makeFriend(emails[0], emails[1]);
		
		return result ? 
				new ResponseEntity<>(new ResponseData(result), HttpStatus.CREATED) :
				new ResponseEntity<>(new ResponseData(result, "Unable to process adding friends"), HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping
	public ResponseEntity<FriendshipListData> list() {
		List<Friendship> list = friendshipService.list();
		FriendshipListData data = FriendsAssembler.toFriendshipListData(list);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@PutMapping(value="/subscribe")
	public ResponseEntity<ResponseData> subscribe(@RequestBody RequestData data) {
		boolean result = friendshipService.subscribe(data.getRequestor(), data.getTarget());
		return result ? 
				new ResponseEntity<>(new ResponseData(result), HttpStatus.CREATED) :
				new ResponseEntity<>(new ResponseData(result, "Unable to process subsciption"), HttpStatus.BAD_REQUEST);
	}
	
	@PutMapping(value="/block")
	public ResponseEntity<ResponseData> block(@RequestBody RequestData data) {
		boolean result = friendshipService.block(data.getRequestor(), data.getTarget());
		return result ? 
				new ResponseEntity<>(new ResponseData(result), HttpStatus.CREATED) :
				new ResponseEntity<>(new ResponseData(result, "Unable to process subsciption"), HttpStatus.BAD_REQUEST);
	}
}
