package com.example.checkout;

import com.example.checkout.CustomGridViewAdapter.RecordHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class dialogAdapter extends ArrayAdapter<Bitmap> {
 int layoutResourceId;
private Bitmap[] pic;
private Context context;
 static class ViewHolder {
     ImageView nameTxVw;
 }
	public dialogAdapter(Context context,int layoutResourceId, Bitmap[] picMap) {
		
		super(context,layoutResourceId, picMap);
		this.layoutResourceId= layoutResourceId;
		this.pic = picMap;
		this.context = context;
	}
	 public View getView(int position, View convertView, ViewGroup parent)
	  {
		 View row = convertView;
			RecordHolder holder = null;
			//check for already existing content
			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new RecordHolder();

				holder.itemImage = (ImageView) row.findViewById(R.id.item_image_dialog);
				row.setTag(holder);
			} else {
				holder = (RecordHolder) row.getTag();
			}

			holder.itemImage.setImageBitmap(pic[position]);
			return row;
	  }
}
