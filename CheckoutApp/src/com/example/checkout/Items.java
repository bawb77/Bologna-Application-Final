package com.example.checkout;

import android.graphics.Bitmap;

//class object for managing grocery items
public class Items {
	public String item;
	public double price;
	public int itemId;
	Bitmap pic;
	int group;
	//Constructor class for adding items
	public Items(String Item, double Price, int Id, Bitmap Pic, int Group)
	{
		item = Item;
		price = Price;
		itemId = Id;
		pic = Pic;
		group = Group;
	}
	public String getItem() {
		return item;
	}
	public double getPrice() {
		return price;
	}
	public int getId()
	{
		return itemId;
	}
	public Bitmap getPic()
	{
		return pic;
	}
	public int getGroup()
	{
		return group;
	}
}
