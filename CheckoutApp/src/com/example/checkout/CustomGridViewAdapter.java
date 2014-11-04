package com.example.checkout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
			holder.itemImage = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
		} 
		else
		{
			holder = (RecordHolder) row.getTag();
		}
		//set data object to the correct textview
		Items item = data.get(position);
		int color = item.getGroup();
		switch(color)
			{
		case 0:
			break;
		case 1:row.setBackgroundColor(Color.GREEN);
			break;
		case 2:row.setBackgroundColor(Color.RED);
			break;
		case 3:row.setBackgroundColor(Color.BLUE);
			break;
		case 4:row.setBackgroundColor(Color.CYAN);
			break;
		case 5:row.setBackgroundColor(Color.MAGENTA);
			break;
		case 6:row.setBackgroundColor(Color.YELLOW);
			break;
		default:
			break;
			}
		holder.itemName.setText(item.getItem());
		holder.itemPrice.setText(Double.toString(item.getPrice()));
		holder.itemImage.setImageBitmap(item.getPic());
		return row;

	}
	//class object for managing inputs
	static class RecordHolder {
		TextView itemName;
		TextView itemPrice;
		ImageView itemImage;

	}
}