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

public class HorizontalListWithButtonAdapter extends BaseAdapter implements
		View.OnClickListener {

	private static final int VIEW_TYPE_NORMAL = 0;
	private static final int VIEW_TYPE_BUTTON = 1;

	private Context mContext;
	private ArrayList<String> mData;
	private ArrayList<Integer> mSelectedIndexes;

	public HorizontalListWithButtonAdapter(Context context,
			ArrayList<String> data) {
		super();
		this.mContext = context;
		this.mData = data;
		this.mSelectedIndexes = new ArrayList<Integer>();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position < mData.size())
			return VIEW_TYPE_NORMAL;
		else
			return VIEW_TYPE_BUTTON;
	}

	@Override
	public int getCount() {
		return mData == null ? 1 : mData.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return mData == null ? null : mData.size() >= position ? null : mData
				.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = getItemViewType(position);
		if (viewType == VIEW_TYPE_NORMAL) {
			HorizontalListItemHolder holder = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				convertView = inflater.inflate(R.layout.child_layout, null);
				holder = new HorizontalListItemHolder();
				holder.button = (Button) convertView.findViewById(R.id.button);
				holder.label = (TextView) convertView.findViewById(R.id.label);
				holder.mark = (ImageView) convertView
						.findViewById(R.id.check_mark);
				holder.index = position;

				holder.button.setOnClickListener(this);
				convertView.setTag(holder);
			} else {
				holder = (HorizontalListItemHolder) convertView.getTag();
			}

			holder.label.setText(mData.get(position));
			if (mSelectedIndexes.contains(holder.index)) {
				holder.mark.setVisibility(View.VISIBLE);
			} else {
				holder.mark.setVisibility(View.INVISIBLE);
			}
		} else {
			if (convertView == null) {
				convertView = new Button(mContext);
				((Button) convertView).setText("click me");
			}
		}
//		HorizontalListItemHolder holder = null;
//		if (convertView == null) {
//			LayoutInflater inflater = LayoutInflater.from(mContext);
//			convertView = inflater.inflate(R.layout.child_layout, null);
//			holder = new HorizontalListItemHolder();
//			holder.button = (Button) convertView.findViewById(R.id.button);
//			holder.label = (TextView) convertView.findViewById(R.id.label);
//			holder.mark = (ImageView) convertView.findViewById(R.id.check_mark);
//			holder.index = position;
//
//			holder.button.setOnClickListener(this);
//			convertView.setTag(holder);
//		} else {
//			holder = (HorizontalListItemHolder) convertView.getTag();
//		}
//
//		holder.label.setText(mData.get(position));
//		if (mSelectedIndexes.contains(holder.index)) {
//			holder.mark.setVisibility(View.VISIBLE);
//		} else {
//			holder.mark.setVisibility(View.INVISIBLE);
//		}
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
