package com.example.checkout;
//class object for managing gorcery items
public class Items {
	private String item;
	private double price;
	//constuctor class for adding items
	public Items(String Item, double Price)
	{
		item = Item;
		price = Price;
	}
	//gets
	public String getItem() {
		return item;
	}
	public double getPrice() {
		return price;
	}
}
