package com.sp.social.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData {
	private boolean success;
	private String message;
	
	public ResponseData(boolean success) {
		this.success = success;
	}
}
