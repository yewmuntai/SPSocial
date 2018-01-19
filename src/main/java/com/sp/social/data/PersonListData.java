package com.sp.social.data;

import java.util.List;

import com.sp.social.model.Person;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class PersonListData extends ResponseData {
	private List<Person> list;
}
