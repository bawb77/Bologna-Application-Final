package com.example.checkout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomGridViewAdapterCart extends ArrayAdapter<CartItems> {
	//Instantiations
	Context context;
	int layoutResourceId;
	ArrayList<CartItems> data = new ArrayList<CartItems>();
	//constuctor class with super call
	public CustomGridViewAdapterCart(Context context, int layoutResourceId,
			ArrayList<CartItems> data) {
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
			holder.itemQuantity = (TextView) row.findViewById(R.id.item_quantity);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		//set data object to the correct textview
		CartItems item = data.get(position);
		holder.itemName.setText(item.getItem());
		holder.itemPrice.setText(Double.toString(item.getPrice()));
		holder.itemQuantity.setText("Quantity: " + Integer.toString(item.getQuantity()));
		return row;

	}
	//class object for managing inputs
	static class RecordHolder {
		TextView itemName;
		TextView itemPrice;
		TextView itemQuantity;

	}
}