package com.example.meetup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import meetup_objects.MeetUpUser;

public class MeetUserList implements Serializable {
	private static final long serialVersionUID = 4466821913603037341L;
	ArrayList<Map<String, MeetUpUser>> mUsers = new ArrayList<Map<String, MeetUpUser>>();

	public MeetUserList(ArrayList<Map<String, MeetUpUser>> users) {
		mUsers = users;
	}

	public ArrayList<Map<String, MeetUpUser>> getList() {
		return mUsers;
	}

	public void setList(ArrayList<Map<String, MeetUpUser>> list) {
		this.mUsers = list;
	}
}
