package com.example.checkout;

public class LogItem {
	public String value;
	public String date;
	public int user_type;
	
	public LogItem(String text, String date, int user){
		this.value = text;
		this.date = date;
		this.user_type = user;
	}
}
