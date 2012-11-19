package com.example.horzlistview;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HorizontalListAdapter extends BaseAdapter implements
		View.OnClickListener {

	private Context mContext;
	private ArrayList<String> mData;

	public HorizontalListAdapter(Context context, ArrayList<String> data) {
		super();
		this.mContext = context;
		this.mData = data;
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData == null ? null : mData.size() <= position ? null : mData
				.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HorizontalListItemHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.child_layout, null);
			holder = new HorizontalListItemHolder();
			holder.button = (Button) convertView.findViewById(R.id.button);
			holder.button.setText(String.valueOf(position));
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.button.setOnClickListener(this);
			holder.index = position;

			convertView.setTag(holder);
		} else {
			holder = (HorizontalListItemHolder) convertView.getTag();
		}

		holder.label.setText((CharSequence) getItem(position));
		return convertView;
	}

	public static class HorizontalListItemHolder {
		ImageView mark;
		Button button;
		TextView label;
		int index;
	}

	@Override
	public void onClick(View v) {

		RelativeLayout parent = (RelativeLayout) v.getParent();
		HorizontalListItemHolder holder = (HorizontalListItemHolder) parent
				.getTag();
		if (mData.get(holder.index).equalsIgnoreCase(" mark ")) {
			mData.set(holder.index, " unmark ");
		} else
			mData.set(holder.index, " mark ");
		notifyDataSetChanged();

	}

}
