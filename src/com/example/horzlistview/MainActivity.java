package com.example.horzlistview;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.sileria.android.view.HorzListView;

public class MainActivity extends Activity {

	private HorzListView mListView;
	private ArrayList<String> mData;
	private HorizontalListAdapter mListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setupData();
		findViews();
	}

	private void setupData() {
		mData = new ArrayList<String>();
		mData.add("1");
		mData.add("2");
		mData.add("3");
		mData.add("4");
		mData.add("5");
		mData.add("6");
		mData.add("7");
		mData.add("8");
		mData.add("9");
		mData.add("10");
		mData.add("11");
		mData.add("12");
		mData.add("13");
		mData.add("14");
		mData.add("15");
		mData.add("16");
		mData.add("17");
		mData.add("18");
		mData.add("19");
		mData.add("20");
		mData.add("21");
		mData.add("22");
		mData.add("23");

	}

	private void findViews() {
		mListView = (HorzListView) findViewById(R.id.horiz_list);

		mListAdapter = new HorizontalListAdapter(this, mData);
		mListView.setAdapter(mListAdapter);

	}

}
