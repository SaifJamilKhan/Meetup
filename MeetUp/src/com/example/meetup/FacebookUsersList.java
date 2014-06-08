package com.example.meetup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class FacebookUsersList implements Serializable {
	private static final long serialVersionUID = 4466821913603037341L;
	ArrayList<Map<String, String>> mUsers = new ArrayList<Map<String, String>>();

	public FacebookUsersList(ArrayList<Map<String, String>> users) {
		mUsers = users;
	}

	public ArrayList<Map<String, String>> getList() {
		return mUsers;
	}

	public void setList(ArrayList<Map<String, String>> list) {
		this.mUsers = list;
	}
}
