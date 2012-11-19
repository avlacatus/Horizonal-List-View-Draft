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
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");
		mData.add(" unmark ");

	}

	private void findViews() {
		mListView = (HorzListView) findViewById(R.id.horiz_list);

		mListAdapter = new HorizontalListAdapter(this, mData);
		mListView.setAdapter(mListAdapter);

	}

}
