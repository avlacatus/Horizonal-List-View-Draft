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
	private ArrayList<Integer> mSelectedIndexes;

	public HorizontalListAdapter(Context context, ArrayList<String> data) {
		super();
		this.mContext = context;
		this.mData = data;
		this.mSelectedIndexes = new ArrayList<Integer>();
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
			holder.label = (TextView) convertView.findViewById(R.id.label);
			holder.mark = (ImageView) convertView.findViewById(R.id.check_mark);
			holder.index = position;

			holder.button.setOnClickListener(this);
			holder.button.setText(String.valueOf(position));
			convertView.setTag(holder);
		} else {
			holder = (HorizontalListItemHolder) convertView.getTag();
		}

		holder.label.setText((CharSequence) getItem(position));
		if (mSelectedIndexes.contains(holder.index)) {
			holder.mark.setVisibility(View.VISIBLE);
		} else {
			holder.mark.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	public static class HorizontalListItemHolder {
		Button button;
		ImageView mark;
		TextView label;
		int index;
	}

	@Override
	public void onClick(View v) {

		RelativeLayout parent = (RelativeLayout) v.getParent();
		HorizontalListItemHolder holder = (HorizontalListItemHolder) parent
				.getTag();
		if (mSelectedIndexes.contains(holder.index)) {
			mSelectedIndexes.remove(Integer.valueOf(holder.index));
		} else {
			mSelectedIndexes.add(holder.index);
		}
		notifyDataSetChanged();

	}
}
