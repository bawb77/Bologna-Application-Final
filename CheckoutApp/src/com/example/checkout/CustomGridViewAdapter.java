package com.example.checkout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomGridViewAdapter extends ArrayAdapter<Items> {
	//Instantiations
	Context context;
	int layoutResourceId;
	ArrayList<Items> data = new ArrayList<Items>();
	//constuctor class with super call
	public CustomGridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Items> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;
		//check for already existing content
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			//assign textviews
			holder.itemName = (TextView) row.findViewById(R.id.item_text);
			holder.itemPrice = (TextView) row.findViewById(R.id.item_price);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		//set data object to the correct textview
		Items item = data.get(position);
		holder.itemName.setText(item.getItem());
		holder.itemPrice.setText(Double.toString(item.getPrice()));
		return row;

	}
	//class object for managing inputs
	static class RecordHolder {
		TextView itemName;
		TextView itemPrice;

	}
}