package com.sp.social.assembler;

import java.util.List;

import com.sp.social.data.PostResultData;

public class PostAssembler {
	public static PostResultData toPostResultData(List<String> emails) {
		PostResultData data = new PostResultData(emails);
		
		return data;
	}
}
