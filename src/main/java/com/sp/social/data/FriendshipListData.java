package com.sp.social.data;

import java.util.List;

import com.sp.social.model.Friendship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipListData extends ResponseData {
	private List<Friendship> list;
}
