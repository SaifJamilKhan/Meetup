package com.example.meetup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import meetup_objects.MeetUpUser;

public class MeetUserList implements Serializable {
	private static final long serialVersionUID = 4466821913603037341L;
	HashMap<String, MeetUpUser> mUsers = new HashMap<String, MeetUpUser>();

	public MeetUserList(HashMap<String, MeetUpUser> users) {
		mUsers = users;
	}

	public HashMap<String, MeetUpUser> getUsers() {
		return mUsers;
	}

	public void setUsers(HashMap<String, MeetUpUser> users) {
		this.mUsers = users;
	}
}
