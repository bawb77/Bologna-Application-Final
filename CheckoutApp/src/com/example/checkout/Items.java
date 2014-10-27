package com.example.checkout;
//class object for managing grocery items
public class Items {
	private String item;
	private double price;
	private int id;
	//Constructor class for adding items
	public Items(String Item, double Price, int Id)
	{
		item = Item;
		price = Price;
		id = Id;
	}
	public String getItem() {
		return item;
	}
	public double getPrice() {
		return price;
	}
	public int getId()
	{
		return id;
	}
}
