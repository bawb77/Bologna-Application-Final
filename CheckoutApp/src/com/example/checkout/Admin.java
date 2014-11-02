package com.example.checkout;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
public class Admin extends Activity {
	//array lists for managing items and inventory
	ArrayList<Items> itemList = new ArrayList<Items>();
	ArrayList<Items> EditedItemList = new ArrayList<Items>();
	ArrayList<CartItems> cartItems = new ArrayList<CartItems>();
	// layout item instantiation
	GridView ItemGrid;
	CustomGridViewAdapter customGridAdapter;
	CustomGridViewAdapterCart customGridAdapterCart;
	double Total;
	boolean discount5;
	boolean discount10;
	EditText searchText;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);

        getWindow().setSoftInputMode(
  		      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       
        //starting Items for Grid
        SqlLiteYouMeanIt db = new SqlLiteYouMeanIt(this);
        itemList = db.getAllItems();
        EditedItemList = db.getAllItems();
        mainListDisplay();
        searchText = (EditText) findViewById(R.id.searchTexta);
        searchText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			}	
        });
        searchText.addTextChangedListener(new TextWatcher(){
        	public void afterTextChanged(Editable s) {
        		editMainList();
        		mainListDisplay();
        	}
			@Override public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			@Override public void onTextChanged(CharSequence s, int start, int before,int count) {}
        });   
    } 
    public void editMainList()
    {
    	EditedItemList.clear();
    	
    	String filter = searchText.getText().toString().toLowerCase();
    	Log.v("ALC", filter);
    	if(!itemList.isEmpty())
    	{
    		for(Items tempItem: itemList)
    		{
    			Log.v("ALC", tempItem.item);
    			if(tempItem.item.toLowerCase().startsWith(filter))
    			{
    				EditedItemList.add(tempItem);
    			}
    		}
    	}
    }
    public void mainListDisplay()
    { 
    	//link GridView to itemlist
    	ItemGrid = (GridView)findViewById(R.id.itemGrid);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, EditedItemList);
        ItemGrid.setAdapter(customGridAdapter);
        //set onClick listener for the GridView
        ItemGrid.setOnItemClickListener(new OnItemClickListener() {
        	@Override
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
				addList(position);
			}
		});
    }
    //add items from the girdview list to the cart list
    public void addList(int i)
    {
  	
    }
    public void updatePN()
    {
    	
    }
    public void deleteP()
    {
    	
    }
    public void returnToCheckout()
    {
    	
    }
    //remove items from the checkout cart and update the totals
    //update the display of the cart with the current arraylist of objects added
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}