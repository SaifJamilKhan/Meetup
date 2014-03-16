package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FriendsActivity extends Activity {

	List<Map<String, String>> friendsList = new ArrayList<Map<String, String>>();

	private SimpleAdapter simpleAdpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		initList();
		simpleAdpt = new SimpleAdapter(this, friendsList,
				android.R.layout.simple_list_item_1, new String[] { "planet" },
				new int[] { android.R.id.text1 });

		ListView lv = (ListView) findViewById(R.id.friends_list_view);
		lv.setAdapter(simpleAdpt);

	}

	private void initList() {
		// We populate the planets
		friendsList.add(createPlanet("planet", "Mercury"));
		friendsList.add(createPlanet("planet", "Mercury"));
		friendsList.add(createPlanet("planet", "Mercury"));
		friendsList.add(createPlanet("planet", "Mercury"));
		friendsList.add(createPlanet("planet", "Mercury"));
	}

	private HashMap<String, String> createPlanet(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}

}
