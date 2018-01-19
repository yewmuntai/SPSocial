package com.sp.social.assembler;

import java.util.ArrayList;
import java.util.List;

import com.sp.social.data.FriendsData;
import com.sp.social.model.Friendship;

public class FriendsAssembler {
	public static FriendsData toFriendsData(List<Friendship> friendships) {
		List<String> emails = new ArrayList<>();
		
		friendships.stream().forEach(fs -> {
			emails.add(fs.getPerson2().getEmail());
		});
		
		FriendsData data = new FriendsData();
		data.setSuccess(true);
		data.setFriends(emails);
		data.setCount(emails.size());
		
		return data;
	}
}
