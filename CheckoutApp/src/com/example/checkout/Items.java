package com.example.checkout;
//class object for managing grocery items
public class Items {
	public String item;
	public double price;
	public int itemId;
	//Constructor class for adding items
	public Items(String Item, double Price, int Id)
	{
		item = Item;
		price = Price;
		itemId = Id;
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
}
