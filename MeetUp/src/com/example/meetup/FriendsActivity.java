package com.example.meetup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.meetup.Utils.DatabaseUtil;

public class FriendsActivity extends Activity {

	List<Map<String, String>> friendsList = new ArrayList<Map<String, String>>();

	private SimpleAdapter simpleAdpt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		initList();
		simpleAdpt = new SimpleAdapter(this, friendsList,
				android.R.layout.simple_list_item_1, new String[] { "name" },
				new int[] { android.R.id.text1 });

		ListView lv = (ListView) findViewById(R.id.friends_list_view);
		lv.setAdapter(simpleAdpt);

	}

	private void initList() {
		// We populate the planets
		friendsList.add(createPlanet("name",
				"" + DatabaseUtil.getCurrentUserName(this)));
		friendsList.add(createPlanet("name", "Friends With Meet Up"));
		friendsList.add(createPlanet("name", "Mercury"));
		friendsList.add(createPlanet("name", "Mercury"));
	}

	private HashMap<String, String> createPlanet(String key, String name) {
		HashMap<String, String> planet = new HashMap<String, String>();
		planet.put(key, name);
		return planet;
	}

}
