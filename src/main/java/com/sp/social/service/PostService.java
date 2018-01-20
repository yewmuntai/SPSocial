package com.sp.social.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sp.social.exception.SPSocialException;
import com.sp.social.model.Friendship;
import com.sp.social.model.Person;
import com.sp.social.model.Post;
import com.sp.social.repository.FriendshipRepository;
import com.sp.social.repository.PersonRepository;
import com.sp.social.repository.PostRepository;

@Service
public class PostService {
	@Autowired
	private PersonService personService;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private FriendshipRepository friendshipRepository;
	
	public List<String> post(String senderEmail, String text) {
		Person sender = personService.getPerson(senderEmail);
		Post post = new Post();
		post.setPoster(sender);
		post.setContent(text);
		postRepository.save(post);

		List<Friendship> followers = friendshipRepository.findByFollowers(sender);
		List<String> receiverEmails = followers.stream().map(fs -> fs.getPerson1().getEmail()).collect(Collectors.toList());

		int idx = text.indexOf('@');
		while (idx > 0) {
			int idx2 = text.indexOf('.', idx+1);
			int idx3 = text.indexOf(". ", idx2+1);
			int idx4 = text.indexOf(' ', idx2+1);

			int endIdx = text.length();
			
			if (idx3 > 0) {
				if (idx3 < idx4 || idx4 < 0) {
					endIdx = idx3;
				}else {
					endIdx = idx4;
				}
			}else if (idx4 > 0) {
				endIdx = idx4;
			}
			
			int startIdx = text.lastIndexOf(' ', idx) + 1;
			String email = text.substring(startIdx, endIdx);
			Optional<Person> person = personRepository.findByEmail(email);
			if (person.isPresent()) {
				receiverEmails.add(email);
			}
			idx = text.indexOf('@', idx+1);
		}
		
		return receiverEmails;
	}
}
