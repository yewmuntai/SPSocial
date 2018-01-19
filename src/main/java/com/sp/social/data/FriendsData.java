package com.sp.social.data;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class FriendsData extends ResponseData {
	List<String> friends;
	int count;
}
