package com.example.checkout;
//class object for managing grocery items
public class CartItems {
	private String item;
	private double price;
	private int quantity;
	//Constructor class for adding items
	public CartItems(String Item, double Price, int Quantity)
	{
		item = Item;
		price = Price;
		quantity = Quantity;
	}
	public String getItem() {
		return item;
	}
	public double getPrice() {
		return price;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int i)
	{
		quantity = i;
	}
}
