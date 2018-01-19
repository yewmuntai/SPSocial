package com.sp.social.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sp.social.data.MakeFriendData;
import com.sp.social.data.ResponseData;
//import com.sp.social.service.PersonService;

@RestController
@RequestMapping(value="/api/friend")
public class FriendController {
	//@Autowired
	//private PersonService personService;

	@PostMapping
	public ResponseEntity<ResponseData> makeFriend(@RequestBody MakeFriendData data) {
		
		return new ResponseEntity<>(new ResponseData(true), HttpStatus.CREATED);
	}
}
